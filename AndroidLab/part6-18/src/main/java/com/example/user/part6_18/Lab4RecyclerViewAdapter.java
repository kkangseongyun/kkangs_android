package com.example.user.part6_18;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */

public class Lab4RecyclerViewAdapter extends RecyclerView.Adapter<ItemHolder> {
    private List<DataVO> list;

    public Lab4RecyclerViewAdapter(List<DataVO> list) {
        this.list = list;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.lab4_sheet_row, parent, false);
        return new ItemHolder(root);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        DataVO vo=list.get(position);
        holder.textView.setText(vo.title);
        holder.imageView.setImageDrawable(vo.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
class ItemHolder extends RecyclerView.ViewHolder{

    TextView textView;
    ImageView imageView;

    public ItemHolder(View root) {
        super(root);
        imageView = (ImageView) itemView.findViewById(R.id.lab4_sheet_row_imageView);
        textView = (TextView) itemView.findViewById(R.id.lab4_sheet_row_textView);
    }
}
class DataVO {
    String title;
    Drawable image;
}
