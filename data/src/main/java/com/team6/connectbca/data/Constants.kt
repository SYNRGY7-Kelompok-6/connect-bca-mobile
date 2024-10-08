package com.team6.connectbca.data

const val BASE_URL = "https://be-java-production-f674.up.railway.app/api/v1.0/"
const val BASE_JS_URL = "https://connect-bca.fly.dev/api/v1.0/"
const val LOGIN_URL = "auth/login"
const val BANK_STATEMENT_URL = "bank-statement"
const val ACCOUNT_MONTHLY_URL = "bank-statement/monthly"
const val VERIFY_QR_URL = "qr/qr-verify"
const val GENERATE_QR_URL = "qr/qr-transfer?option=url"
const val TRASNFER_INTRABANK_URL = "transfer-intrabank"
const val TRANSFER_DETAIL = "bank-statement/mutations/detail"
const val SAVED_ACCOUNTS_URL = "saved-accounts"
const val SAVED_ACCOUNTS_DETAIL = "saved-accounts/{savedBeneficiaryId}"
const val PIN_VALIDATE = "auth/validate-pin"
const val LATEST_INCOME = "bank-statement/latest-income"
const val USER_PROFILE = "users/profile"