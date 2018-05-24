package com.danielakinola.loljournal.matchupdetail;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.ViewModelFactory;
import com.danielakinola.loljournal.data.models.Comment;
import com.danielakinola.loljournal.databinding.FragmentMatchupDetailBinding;
import com.danielakinola.loljournal.databinding.ItemCommentBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class MatchupDetailFragment extends android.support.v4.app.Fragment {
    private static final String TITLE = "TITLE";
    private static final String CATEGORY = "CATERGORY";

    private String title;
    private int category;
    @Inject
    ViewModelFactory viewModelFactory;
    private MatchupDetailViewModel matchupDetailViewModel;


    public MatchupDetailFragment() {
        // Required empty public constructor
    }

    public static MatchupDetailFragment newInstance(int category, String title) {
        MatchupDetailFragment fragment = new MatchupDetailFragment();
        Bundle args = new Bundle();
        args.putInt(CATEGORY, category);
        args.putString(TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(TITLE);
            category = getArguments().getInt(CATEGORY);
        }
        matchupDetailViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), viewModelFactory).get(MatchupDetailViewModel.class);
    }

    private void setupRecyclerView(View rootView) {
        RecyclerView recyclerView = Objects.requireNonNull(rootView.findViewById(R.id.comment_recyler_view));
        View emptyState = rootView.findViewById(R.id.empty_state);
        CommentAdapter commentAdapter = new CommentAdapter();
        recyclerView.setAdapter(commentAdapter);

        matchupDetailViewModel.getComments(category).observe(this, comments -> {
            if (comments == null || comments.isEmpty()) {
                emptyState.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                emptyState.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                commentAdapter.setComments(comments);
                commentAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_matchup_detail, container, false);

        FragmentMatchupDetailBinding fragmentMatchupDetailBinding = FragmentMatchupDetailBinding.bind(rootView);
        fragmentMatchupDetailBinding.setViewmodel(matchupDetailViewModel);
        fragmentMatchupDetailBinding.setCategory(category);

        setupRecyclerView(rootView);
        return rootView;
    }


    class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

        ArrayList<Comment> comments = new ArrayList<>();

        CommentAdapter() {

        }


        public void setComments(List<Comment> comments) {
            this.comments = (ArrayList<Comment>) comments;
        }

        @NonNull
        @Override
        public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ItemCommentBinding itemCommentBinding = ItemCommentBinding.inflate(inflater, parent, false);
            itemCommentBinding.setListener(new CommentActionListener() {
                @Override
                public void onCommentFavourited(Comment comment) {
                    matchupDetailViewModel.changeCommentFavourited(comment);
                }

                @Override
                public void onCommentPlusOned() {
                    matchupDetailViewModel.onCommentPlusOned();
                }

                @Override
                public void onCommentSelected(Comment comment) {
                    matchupDetailViewModel.onCommentSelected(comment);
                }
            });

            return new CommentViewHolder(itemCommentBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
            holder.bind(comments.get(position));
        }

        @Override
        public int getItemCount() {
            return comments == null ? 0 : comments.size();
        }

        class CommentViewHolder extends RecyclerView.ViewHolder {

            private final ItemCommentBinding itemCommentBinding;

            public CommentViewHolder(ItemCommentBinding itemCommentBinding) {
                super(itemCommentBinding.getRoot());
                this.itemCommentBinding = itemCommentBinding;
            }

            void bind(Comment comment) {
                itemCommentBinding.setComment(comment);
                itemCommentBinding.executePendingBindings();
            }
        }
    }
}
