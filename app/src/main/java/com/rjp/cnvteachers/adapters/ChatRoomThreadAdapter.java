package com.rjp.cnvteachers.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.beans.FcmNotificationBean;
import com.rjp.cnvteachers.utils.AppPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Shraddha on 4/29/2017.
 */

public class ChatRoomThreadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static String TAG = ChatRoomThreadAdapter.class.getSimpleName();

    private String userId;
    private int SELF = 100;
    private static String today;

    private Context mContext;
    //private ArrayList<Message> messageArrayList;
    private ArrayList<FcmNotificationBean> messageArrayList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, message, timestamp;

        public ViewHolder(View view) {
            super(view);
            message = (TextView) itemView.findViewById(R.id.message);
            title = (TextView) itemView.findViewById(R.id.title);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
        }
    }

    public ChatRoomThreadAdapter(Context mContext, ArrayList<FcmNotificationBean> messageArrayList, String userId) {
        this.mContext = mContext;
        this.messageArrayList = messageArrayList;
        this.userId = userId;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        // view type is to identify where to render the chat message
        // left or right
        if (viewType == SELF) {
            // self message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_self, parent, false);
        } else {
            // others message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_other, parent, false);
        }


        return new ViewHolder(itemView);
    }


    @Override
    public int getItemViewType(int position) {
        FcmNotificationBean message = messageArrayList.get(position);
        if (message.getSender_id().equals(AppPreferences.getLoginObj(mContext).getEmpid())) {
            return SELF;
        }

        /*if (message.getUser().getId().equals(userId)) {
            return SELF;
        }*/

        return position;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position)
    {
        FcmNotificationBean message = messageArrayList.get(position);

        ((ViewHolder) holder).message.setText(message.getMessage());


        String timestamp = getTimeStamp(message.getEntry_date());

        if (message.getSender_name() != null)
            //timestamp = message.getSender_name() + ", " + timestamp;

            ((ViewHolder) holder).timestamp.setText(timestamp);

        if(position==0)
        {
            ((ViewHolder) holder).title.setVisibility(View.VISIBLE);
            ((ViewHolder) holder).title.setText(message.getTitle());
        }
        else
        {
            if(!message.getTitle().equals(messageArrayList.get(position-1).getTitle()))
            {
                ((ViewHolder) holder).title.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).title.setText(message.getTitle());
            }
            else
            {
                ((ViewHolder) holder).title.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    public static String getTimeStamp(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = "";

        today = today.length() < 2 ? "0" + today : today;

        try {
            Date date = format.parse(dateStr);
            SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
            String dateToday = todayFormat.format(date);
            format = dateToday.equals(today) ? new SimpleDateFormat("hh:mm a") : new SimpleDateFormat("dd LLL, hh:mm a");
            String date1 = format.format(date);
            timestamp = date1.toString();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }
}
