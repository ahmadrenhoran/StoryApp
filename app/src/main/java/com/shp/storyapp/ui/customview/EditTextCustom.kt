package com.shp.storyapp.ui.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.shp.storyapp.R
import com.shp.storyapp.utils.isValidEmail


class EditTextCustom : AppCompatEditText {

    private fun init() {

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if ((inputType >= EditorInfo.TYPE_TEXT_VARIATION_PASSWORD) && (inputType <= EditorInfo.TYPE_TEXT_VARIATION_PASSWORD + 1)) {
                    if (s.length < 6) {
                        setError(resources.getString(R.string.Password_Error) + " 6", null)
                    } else {
                        error = null
                    }
                } else if ((inputType >= EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) && (inputType <= EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS + 1)) {
                    if (!isValidEmail(s)) {
                        setError(resources.getString(R.string.Email_Error), null)
                    } else {
                        error = null
                    }
                }


            }

            override fun afterTextChanged(s: Editable) {

            }
        })

    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }


    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }


}