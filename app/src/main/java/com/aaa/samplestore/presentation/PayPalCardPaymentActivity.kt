package com.aaa.samplestore.presentation

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.paypal.android.cardpayments.CardApproveOrderCallback
import com.paypal.android.cardpayments.CardApproveOrderResult

class PayPalCardPaymentActivity: FragmentActivity(), CardApproveOrderCallback {
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
    }

    override fun onCardApproveOrderResult(result: CardApproveOrderResult) {
        TODO("Not yet implemented")
    }

}