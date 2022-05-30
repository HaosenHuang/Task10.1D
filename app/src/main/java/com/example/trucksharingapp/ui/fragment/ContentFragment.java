package com.example.trucksharingapp.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trucksharingapp.R;
import com.example.trucksharingapp.db.AppDataBase;
import com.example.trucksharingapp.model.OrderModel;
import com.example.trucksharingapp.ui.MainActivity;
import com.example.trucksharingapp.ui.orderDetails.OrderDetailsActivity;
import com.example.trucksharingapp.utli.BaseRecyclerAdapter;
import com.example.trucksharingapp.utli.BaseRecyclerHolder;
import com.example.trucksharingapp.utli.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ContentFragment extends Fragment {
    private boolean isHome;
    private MainActivity mainActivity;
    private ImageView moreButton;
    private TextView titleTextView;
    private TextView rightButton;
    private List<OrderModel> data = new ArrayList<>();
    private RecyclerView recyclerView;
    private BaseRecyclerAdapter<OrderModel> adapter;


    public ContentFragment() {
    }

    public ContentFragment(boolean isHome, MainActivity mainActivity) {
        this.isHome = isHome;
        this.mainActivity = mainActivity;
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_content, container, false);
        EventBus.getDefault().register(this);

        initView(inflate);
        initData();
        return inflate;
    }

    private void initData() {
        //Judge whether it is a home page. If it is a home page, query all, otherwise only your own
        if (isHome) {
            data = AppDataBase.getInstance().orderDao().getAll();
        } else {
            data = AppDataBase.getInstance().orderDao().getByUserId(mainActivity.currentUserData.userId);
        }
        data.sort((orderModel, t1) -> t1.getCreateTime() - orderModel.getCreateTime());

        Log.e("TAG", "initData: " + data.size());
        adapter = new BaseRecyclerAdapter<OrderModel>(getContext(),data,R.layout.item_order) {
            @Override
            public void convert(BaseRecyclerHolder holder, OrderModel item, int position, boolean isScrolling) {
                holder.setText(R.id.nameTextView,"Receiver name: "+item.getReceiverName());
                holder.setText(R.id.dateTextView,"Pick up date: "+item.getPickDate());
                holder.setText(R.id.timeTextView,"Pick up time: "+item.getPickTime());
                holder.setText(R.id.locationTextView,"Pick up location: "+item.getLocation());
                holder.setText(R.id.dropLocationTextView,"Drop-off location: "+item.getDropOffLocation());

                //click share
                holder.getView(R.id.share).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String shareText = "Receiver name: "+item.getReceiverName()+"\nPick up date: "+item.getPickDate()+"\nPick up time: "+item.getPickTime()+"\nPick up location: "+item.getLocation();
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                        shareIntent = Intent.createChooser(shareIntent, "");
                        startActivity(shareIntent);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
                intent.putExtra("data",data.get(position));
                startActivity(intent);
            }
        });
    }

    private void initView(View inflate) {
        moreButton = (ImageView) inflate.findViewById(R.id.backButton);
        moreButton.setVisibility(View.VISIBLE);
        moreButton.setImageResource(R.mipmap.more_image);
        titleTextView = (TextView) inflate.findViewById(R.id.titleTextView);
        recyclerView = (RecyclerView)inflate.findViewById(R.id.recyclerView);
        if (isHome) {
            titleTextView.setText("Home");
        } else {

            titleTextView.setText("My order");
        }
        rightButton = (TextView) inflate.findViewById(R.id.rightButton);
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickMoreButton();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    private void clickMoreButton() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_popup, null, false);
        TextView home = view.findViewById(R.id.home);
        TextView account = view.findViewById(R.id.account);
        TextView myOrder = view.findViewById(R.id.myorder);

        final PopupWindow popWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popWindow.setAnimationStyle(R.anim.anim_pop);


        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;

            }
        });
        popWindow.showAsDropDown(moreButton, 50, 0);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Click account",Toast.LENGTH_SHORT).show();
                popWindow.dismiss();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isHome) {
                    mainActivity.changeFragment(true);
                }
                popWindow.dismiss();
            }
        });
        myOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHome) {
                    mainActivity.changeFragment(false);
                }
                popWindow.dismiss();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refOrder(MessageEvent messageEvent) {
        Log.e("TAG", "refOrder: " + messageEvent.getMessage());
        if (isHome) {
            data = AppDataBase.getInstance().orderDao().getAll();
        } else {
            data = AppDataBase.getInstance().orderDao().getByUserId(mainActivity.currentUserData.userId);
        }
        Log.e("TAG", "refOrder: "+data.size() );
        data.sort((orderModel, t1) -> t1.getCreateTime() - orderModel.getCreateTime());
        adapter.addAll(data);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}