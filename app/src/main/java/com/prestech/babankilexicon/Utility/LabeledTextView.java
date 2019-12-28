package com.prestech.babankilexicon.Utility;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prestech.babankilexicon.R;

public class LabeledTextView extends LinearLayout {
    LinearLayout mLinearLayout = null;
    TextView labelTv = null;
    TextView valueTv = null;
    Context mContext;

    // Overwrites constructor
    public LabeledTextView(Context context) {
        super(context);
        mContext = context;
    }

    // Overwrites constructor
    public LabeledTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LabeledTextView);

        String leftText = a.getString(R.styleable.LabeledTextView_labelText);
        String rightText = a.getString(R.styleable.LabeledTextView_valueText);

        leftText = leftText == null ? "" : leftText;
        rightText = rightText == null ? "" : rightText;

        String service = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(service);

        mLinearLayout = (LinearLayout) li.inflate(R.layout.labeled_text_view, this, true);

        labelTv = mLinearLayout.findViewById(R.id.label);
        valueTv = mLinearLayout.findViewById(R.id.value);

        labelTv.setText(String.format("%s: ", leftText));
        valueTv.setText(rightText);

        a.recycle();
    }

    // Overwrites constructor
    public LabeledTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    /**
     * Sets the value of the labeled text
     * @param value string value to set. If null, the visibility of the labeled view is set to
     *              VIEW.GONE
     */
    public void setValueText(String value) {
        if (value == null) {
            mLinearLayout.setVisibility(GONE);
            return;
        }
        valueTv.setText(value);
    }
}
