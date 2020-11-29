package cegepst.example.shareparks.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cegepst.example.shareparks.R;
import cegepst.example.shareparks.models.Post;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private final ArrayList<Post> posts;

    public FeedAdapter(ArrayList<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_post_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.setContent(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView author;
        private final ImageView image;
        private final TextView nbrLikes;
        private final TextView caption;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.authorNamePost);
            image = itemView.findViewById(R.id.imagePost);
            nbrLikes = itemView.findViewById(R.id.nbrLikesPost);
            caption = itemView.findViewById(R.id.descriptionPost);
        }

        public void setContent(Post post) {
            author.setText(post.getAuthor());
            image.setImageBitmap(post.getImage());
            nbrLikes.setText(post.getNbrLikes() + " likes");
            caption.setText(post.getCaption());
        }
    }
}
