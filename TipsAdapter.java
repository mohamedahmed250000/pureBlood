package elteam.pureblood.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import elteam.pureblood.R;
import elteam.pureblood.activities.DetailsTipsActivity;
import elteam.pureblood.models.ItemTips;

import static elteam.pureblood.Constants.KEY_Tip;

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.TipsViewHolder> {

    private Context context;
    private List<ItemTips> tips;
    private OnTipClickListener onTipClickListener;

    public TipsAdapter(Context context, List<ItemTips> tips, OnTipClickListener listener) {
        this.context = context;
        this.tips = tips;
        this.onTipClickListener = listener;
    }

    public interface OnTipClickListener{
        void onTipClick(int position);
    }

    @NonNull
    @Override
    public TipsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TipsViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_tips, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TipsViewHolder tipsViewHolder, int i) {

        ItemTips itemTips = tips.get(i);

        tipsViewHolder.textView.setText(itemTips.getQuestion());

        tipsViewHolder.imageView.setImageResource(itemTips.getImage());
    }

    @Override
    public int getItemCount() {
        if (tips.isEmpty()) {
            return 0;
        } else {
            return tips.size();
        }
    }

    class TipsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView textView;
        private View view_tips;

        public TipsViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_tips);
            textView = itemView.findViewById(R.id.question_tips);
            view_tips = itemView.findViewById(R.id.view_tips);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onTipClickListener.onTipClick(getAdapterPosition());
        }
    }
}
