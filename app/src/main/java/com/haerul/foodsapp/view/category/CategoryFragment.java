package com.haerul.foodsapp.view.category;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.haerul.foodsapp.R;
import com.haerul.foodsapp.Utils;
import com.haerul.foodsapp.adapter.RecyclerViewMealByCategory;
import com.haerul.foodsapp.model.Meals;
import com.haerul.foodsapp.view.detail.DetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.haerul.foodsapp.view.home.HomeActivity.EXTRA_DETAIL;

public class CategoryFragment extends Fragment implements CategoryView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.imageCategory)
    ImageView imageCategory;
    @BindView(R.id.imageCategoryBg)
    ImageView imageCategoryBg;
    @BindView(R.id.textCategory)
    TextView textCategory;

    AlertDialog.Builder descDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        View view = inflater.inflate ( R.layout.fragment_category , container , false );
        ButterKnife.bind ( this , view );
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view , @Nullable Bundle savedInstanceState) {
        super.onViewCreated ( view , savedInstanceState );

        if ( getArguments ( ) != null ) {
            textCategory.setText ( getArguments ( ).getString ( "EXTRA_DATA_DESC" ) );
            Picasso.get ( )
                    .load ( getArguments ( ).getString ( "EXTRA_DATA_IMAGE" ) )
                    .into ( imageCategory );
            Picasso.get ( )
                    .load ( getArguments ( ).getString ( "EXTRA_DATA_IMAGE" ) )
                    .into ( imageCategoryBg );
            descDialog = new AlertDialog.Builder ( getActivity ( ) )
                    .setTitle ( getArguments ( ).getString ( "EXTRA_DATA_NAME" ) )
                    .setMessage ( getArguments ( ).getString ( "EXTRA_DATA_DESC" ) );

            CategoryPresenter presenter = new CategoryPresenter ( this );
            presenter.getMealByCategory ( getArguments ( ).getString ( "EXTRA_DATA_NAME" ) );
        }
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility ( View.VISIBLE );
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility ( View.GONE );
    }

    @Override
    public void setMeals(List<Meals.Meal> meals) {
        RecyclerViewMealByCategory adapter =
                new RecyclerViewMealByCategory ( getActivity ( ) , meals );
        recyclerView.setLayoutManager ( new GridLayoutManager ( getActivity ( ) , 2 ) );
        recyclerView.setClipToPadding ( false );
        recyclerView.setAdapter ( adapter );
        adapter.notifyDataSetChanged ( );

        adapter.setOnItemClickListener ( (view , position) -> {
            ImageView imageView = view.findViewById ( R.id.mealThumb );
            TextView mealName = view.findViewById ( R.id.mealName );
            Intent intent = new Intent(getActivity (), DetailActivity.class);
            intent.putExtra(EXTRA_DETAIL, mealName.getText ().toString ());

            Pair<View, String> pair = Pair.create ( (View) imageView, ViewCompat.getTransitionName(imageView) );
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation ( Objects.requireNonNull ( getActivity ( ) ) , pair );

            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
                startActivity ( intent , optionsCompat.toBundle ( ) );
            } else {
                startActivity ( intent );
            }

        } );
    }

    @Override
    public void onErrorLoading(String message) {
        Utils.showDialogMessage ( getActivity ( ) , "Error " , message );
    }

    @OnClick(R.id.cardCategory)
    public void onClick() {
        descDialog.setPositiveButton ( "CLOSE" , (dialog , which) -> dialog.dismiss ( ) );
        descDialog.show ( );
    }

}
