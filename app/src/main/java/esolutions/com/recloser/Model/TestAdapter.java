package esolutions.com.recloser.Model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import esolutions.com.recloser.Entity.TestEntity;
import esolutions.com.recloser.R;

/**
 * Created by VinhNB on 3/5/2017.
 */

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {
    private List<TestEntity> testEntities;
    private Context context;

    public TestAdapter(Context context, List<TestEntity> testEntities) {
        this.context = context;
        this.testEntities = testEntities;
    }

    @Override
    public TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.row_all_device, parent, false);
        TestViewHolder testViewHolder = new TestViewHolder(view);
        return testViewHolder;
    }

    @Override
    public void onBindViewHolder(TestViewHolder holder, int position) {
        String nameDevice = testEntities.get(position).getmDeviceName();
        holder.tvName.setText(nameDevice);
    }

    @Override
    public int getItemCount() {
        return testEntities.size();
    }

    public static class TestViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;    

        public TestViewHolder(View itemView) {
            super(itemView);
//            tvDescription = (TextView) itemView.findViewById(R.id.tv_row_all_device_name);
        }
    }
}
