package com.helper.Helper2Go.adapter;
import android.view.View;
import android.widget.TextView;

import com.helper.Helper2Go.R;
import com.helper.Helper2Go.models.MessageData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SendMessageViewHolder extends RecyclerView.ViewHolder {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

   /* @BindView(R.id.name)
    TextView name;*/
   TextView txt_sent;
    TextView txt_time;


    public SendMessageViewHolder(View view) {
        super(view);
        txt_sent = view.findViewById(R.id.txt_sent);
        txt_time = view.findViewById(R.id.txt_time);
    }

    public void bind(MessageData model, int position) {

        //name.setText(model.getNameTitle());
        txt_sent.setText(model.getMessage());

        String time = getFormatedDateTime(model.getCreatedAt(), "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy, hh:mm a");
        txt_time.setText(time);
    }

    public static String getFormatedDateTime(String dateStr, String strReadFormat, String strWriteFormat) {

        String formattedDate = dateStr;

        DateFormat readFormat = new SimpleDateFormat(strReadFormat, Locale.getDefault());
        DateFormat writeFormat = new SimpleDateFormat(strWriteFormat, Locale.getDefault());

        Date date = null;

        try {
            date = readFormat.parse(dateStr);
        } catch (ParseException e) {
        }

        if (date != null) {
            formattedDate = writeFormat.format(date);
        }

        return formattedDate;
    }

}
