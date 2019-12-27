package com.example.finalassignment.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalassignment.API.APIEndpoints;
import com.example.finalassignment.R;
import com.example.finalassignment.activities.EditTextActivity;
import com.example.finalassignment.models.Datum;
import com.example.finalassignment.models.DeleteResponse;

//import com.example.finalassignment.models.TaskResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.finalassignment.API.ApiClient.getClient;

public class TaskAdapters extends RecyclerView.Adapter<TaskAdapters.Holder>{

    private Context context;
    private List<Datum> listTask;
    private int selectedPosition = -1;

    public TaskAdapters(Context context, List<Datum> listTask) {
        this.context =context;
        this.listTask = listTask;
    }



    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvName,tvTime, tvDay;
        CheckBox taskStatus;
        Button editButton;

        Holder(@NonNull View itemView) {
            super(itemView);

            tvName= itemView.findViewById(R.id.task_name);
            tvTime=itemView.findViewById(R.id.clock_view);
            tvDay=itemView.findViewById(R.id.date_view);
            taskStatus=itemView.findViewById(R.id.checkbox_task);
//            editButton=itemView.findViewById(R.id.editButton);
            taskStatus.setOnClickListener(this::onClick);
            itemView.setOnClickListener(v -> {
                selectedPosition=this.getAdapterPosition();
                Intent intent=new Intent(context, EditTextActivity.class);
                intent.putExtra("TASK_DETAIL",listTask.get(selectedPosition).getTaskName());
                intent.putExtra("TASK_TIME",listTask.get(selectedPosition).getDueTime());
                intent.putExtra("TASK_DATE",listTask.get(selectedPosition).getDueDate());
                intent.putExtra("TASK_ID",listTask.get(selectedPosition).getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            });

        }

        @Override
        public void onClick(View v) {
            selectedPosition=this.getAdapterPosition();

            APIEndpoints apiInterface = getClient().create(APIEndpoints.class);
            apiInterface.deleteTask(listTask.get(selectedPosition).getTaskId()).enqueue(new Callback<DeleteResponse>() {
                @Override
                public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                    Toast.makeText(context, "deleted!!!", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onFailure(Call<DeleteResponse> call, Throwable t) {

                }
            });
            notifyDataSetChanged();
        }
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View v = LayoutInflater.from(context).inflate(R.layout.task_list, viewGroup, false);
//        return new Holder(v);
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.task_list, viewGroup, false);

        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {

        holder.tvName.setText(listTask.get(i).getTaskName());
        holder.tvTime.setText(listTask.get(i).getDueTime().trim());
        holder.tvDay.setText(listTask.get(i).getDueDate().trim());

    }

    @Override
    public int getItemCount() {
        return listTask.size();
    }


}
