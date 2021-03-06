package com.example.libeery.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libeery.R;
import com.example.libeery.adapters.SearchBeerAdapter;
import com.example.libeery.model.BeerRoom;
import com.example.libeery.viewModel.BeersViewModel;
import com.example.libeery.viewModel.BeersViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class SearchBeerFragment extends Fragment {

    public static final String TEXT_IN_SEARCHBAR = "TextSearchView";

    private RecyclerView recyclerView;
    private SearchView beerSearchView;
    private SearchBeerAdapter adapter;
    private BeersViewModel viewModel;
    private List<BeerRoom> beers = new ArrayList<>();
    private String textSearchView;

    public SearchBeerFragment() {}

    public static SearchBeerFragment newInstance() {
        return new SearchBeerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_beer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BeersViewModelFactory factory = BeersViewModelFactory.getInstance();
        factory.initFactory(getActivity().getApplication());
        viewModel = new ViewModelProvider(requireActivity(), factory).get(BeersViewModel.class);

        initRecyclerView();
        observeData();
    }

    private void initRecyclerView() {
        recyclerView = getView().findViewById(R.id.recyclerViewSearch);
        beerSearchView = getView().findViewById(R.id.searchBeerText);
        RecyclerView.LayoutManager layoutManager;

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            layoutManager = new GridLayoutManager(getActivity(),2 );
        else
            layoutManager = new LinearLayoutManager(getActivity());

        adapter = new SearchBeerAdapter(viewModel, beers);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(true);
        beerSearchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        beerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                textSearchView = newText;
                if(beerSearchView.getWidth()>0)
                    adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void observeData() {
        viewModel.getBeerList().observe(getViewLifecycleOwner(), list -> {
            this.beers = new ArrayList<>(list);
            adapter.updateBeers(beers);
            if(list.size()!=0){
                getView().findViewById(R.id.progressBar).setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null)
            beerSearchView.setQuery(textSearchView, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TEXT_IN_SEARCHBAR, textSearchView);
    }
}
