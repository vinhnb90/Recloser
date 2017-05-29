package esolutions.com.recloser.Model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.zip.Inflater;

import esolutions.com.recloser.R;

/**
 * class show layout disconnect recycler view
 * Created by VinhNB on 3/23/2017.
 */

public class DisconnectWifiAdapter extends RecyclerView.Adapter<DisconnectWifiAdapter.DisconnectWifiAdapterViewHolder> {
    private String mMessageError;
    private boolean mIsResponse;

    public DisconnectWifiAdapter(String mMessageError, boolean mIsResponse) {
        this.mMessageError = mMessageError;
        this.mIsResponse = mIsResponse;
    }

    @Override
    public DisconnectWifiAdapter.DisconnectWifiAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.row_error_disconnect, parent, false);
        DisconnectWifiAdapter.DisconnectWifiAdapterViewHolder viewHolder = new DisconnectWifiAdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DisconnectWifiAdapterViewHolder holder, int position) {

        holder.tvError.setText(mMessageError);

        if (mIsResponse)
            holder.ivImage.setBackgroundResource(R.drawable.ic_no_data);
        else
            holder.ivImage.setBackgroundResource(R.drawable.ic_disconnect);

    }


    @Override
    public int getItemCount() {
        return 1;
    }

    public static class DisconnectWifiAdapterViewHolder extends RecyclerView.ViewHolder {
        public TextView tvError;
        public ImageView ivImage;

        public DisconnectWifiAdapterViewHolder(View itemView) {
            super(itemView);
            tvError = (TextView) itemView.findViewById(R.id.tv_row_error_disconnect_error);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_row_error_disconnect_image);
        }
    }
}
