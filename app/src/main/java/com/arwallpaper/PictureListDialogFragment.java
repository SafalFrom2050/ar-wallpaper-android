package com.arwallpaper;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arwallpaper.databinding.FragmentPictureListDialogItemBinding;
import com.arwallpaper.databinding.FragmentPictureListDialogBinding;
import com.google.android.material.imageview.ShapeableImageView;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     PictureListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class PictureListDialogFragment extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_COUNT = "item_count";
    private FragmentPictureListDialogBinding binding;

    // TODO: Customize parameters
    public static PictureListDialogFragment newInstance(int itemCount) {
        final PictureListDialogFragment fragment = new PictureListDialogFragment();
        final Bundle args = new Bundle();
        args.putInt(ARG_ITEM_COUNT, itemCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentPictureListDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(new PictureAdapter(getArguments().getInt(ARG_ITEM_COUNT)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ShapeableImageView image;
        ImageView iconCheck;

        ViewHolder(FragmentPictureListDialogItemBinding binding) {
            super(binding.getRoot());
            image = binding.image;
            iconCheck = binding.iconCheck;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            PictureAdapter adapter = (PictureAdapter) getBindingAdapter();

            int prevPos = adapter.selectedPosition;
            int currPos = getBindingAdapterPosition();

            adapter.setSelectedPosition(currPos);
            adapter.notifyItemChanged(prevPos);
            adapter.notifyItemChanged(currPos);


        }
    }

    private class PictureAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final int mItemCount;
        int selectedPosition = 0;


        PictureAdapter(int itemCount) {
            mItemCount = itemCount;
        }

        public void setSelectedPosition(int selectedPosition) {
            this.selectedPosition = selectedPosition;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new ViewHolder(FragmentPictureListDialogItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.image.setImageResource(R.drawable.wallpaper);
            if (position == selectedPosition) {
                holder.iconCheck.setVisibility(View.VISIBLE);
            }else{
                holder.iconCheck.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return mItemCount;
        }

    }
}