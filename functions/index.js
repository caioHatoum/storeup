const functions = require('firebase-functions');
const admin = require('firebase-admin');
const stripe = require('stripe')('sk_test_n679SOAo9B36Yke6XKOmll9b00Zdb80iLA');

admin.initializeApp({
            apiKey: "AIzaSyCs3d-qO2DhqO2ogR9jU9DV2gkEoDZ8cIA",
            authDomain: "storeupp-19002.firebaseapp.com",
            databaseURL: "https://storeupp-19002.firebaseio.com",
            storageBucket: "storeupp-19002.appspot.com",
            messagingSenderId: "159545217996"
  });

exports.createStripeCustomer = functions.auth.user()
    .onCreate(
        async (user) => {
            const customer = await stripe.customers.create({
                email: user.email
            });
            await admin.firestore()
            .collection('stripe_customers')
            .doc(user.uid)
            .set({
                customer_id:customer.id
            });
        }
    );

exports.addPaymentMethod = functions.https
    .document('/stripe_customers/{userId}/tokens/{autoId}')
    .onWrite(
        async(change,context)=>{
            const data = change.after.data()
            if (data===null){return null}
            const token = data.token
            const snapshot = await admin.firestore()
                .collection('stripe_customers')
                .doc(context.params.userId).get()
            const customer = snapshot.data().customer_id
            const res = await
                stripe.customers.createSource(customer,{source:token})
                await admin.firestore()
                .collection('stripe_customers')
                .set(res,{merge:true})

        })

exports.createStripeCharge = functions.firestore
    .document('/stripe_customers/{userId}/charges/{autoId}')
    .onCreate(
        async (snap,context)=>{
            try{
                const customer = getCustomerId(context.params.userId)
                const amount = snap.data().amount
                const charge = {amount, currency, customer}
                setSourceOrDefault(charge, snap.data().source)
                const idempotencyKey = context.params.autoId
                const res = await stripe.charges.create(charge,{idempotency_key:idempotencyKey})
                await snap.ref.set(res,{merge:true})
            }catch(err){
                await snap.ref.set({err:userFacingMessage(err)})
            }
        }
    )



