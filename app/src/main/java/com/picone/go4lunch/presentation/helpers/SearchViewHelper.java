package com.picone.go4lunch.presentation.helpers;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;

import com.picone.core.domain.entity.user.User;
import com.picone.go4lunch.R;
import com.picone.go4lunch.presentation.ui.main.MainActivity;
import com.picone.go4lunch.presentation.viewModels.RestaurantViewModel;
import com.picone.go4lunch.presentation.viewModels.UserViewModel;

import java.util.List;

import static com.picone.core.utils.ConstantParameter.MAPS_KEY;

public class SearchViewHelper {

    MainActivity mainActivity;
    RestaurantViewModel mRestaurantViewModel;
    UserViewModel mUserViewModel;

    public SearchViewHelper(MainActivity mainActivity, RestaurantViewModel mRestaurantViewModel, UserViewModel mUserViewModel) {
        this.mainActivity = mainActivity;
        this.mRestaurantViewModel = mRestaurantViewModel;
        this.mUserViewModel = mUserViewModel;
    }

    public void initSearchView(MenuItem item) {
        SearchManager searchManager = (SearchManager) mainActivity.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = item.getActionView().findViewById(R.id.top_nav_search_button);
        searchView.onActionViewExpanded();
        assert searchManager != null;
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(mainActivity.getComponentName()));
        searchView.setQueryHint(mainActivity.getString(R.string.search_restaurants));
        searchView.setBackgroundColor(Color.WHITE);
        searchView.setOnQueryTextListener(getOnQueryTextListener());
        item.setOnActionExpandListener(geOnActionExpandListener(searchView));
        View deleteButton = searchView.findViewById(R.id.search_close_btn);
        deleteButton.setOnClickListener(getOnClickListener(searchView));
    }

    @NonNull
    private View.OnClickListener getOnClickListener(SearchView searchView) {
        return v -> {
            EditText editText = mainActivity.findViewById(R.id.search_src_text);
            editText.setText("");
            searchView.setQuery("", false);
            mRestaurantViewModel.setAllRestaurantFromMaps(true);
            mUserViewModel.setAllDbUsers();
        };
    }

    @NonNull
    private MenuItem.OnActionExpandListener geOnActionExpandListener(SearchView searchView) {
        return new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                searchView.setIconified(false);
                searchView.setOnQueryTextListener(getOnQueryTextListener());
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mRestaurantViewModel.setAllRestaurantFromMaps(true);
                mUserViewModel.setAllDbUsers();
                return true;
            }
        };
    }

    private SearchView.OnQueryTextListener getOnQueryTextListener() {
        List<User> allUsers = mUserViewModel.getAllUsers().getValue();
        Location location = mRestaurantViewModel.getCurrentLocation.getValue();
        assert location != null;
        String locationStr = String.valueOf(location.getLatitude()).concat(",").concat(String.valueOf(location.getLongitude()));
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mRestaurantViewModel.filterExistingResults(query, allUsers, locationStr, MAPS_KEY);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() < 2) {
                    return false;
                }
                mRestaurantViewModel.filterExistingResults(newText, allUsers, locationStr, MAPS_KEY);
                return true;
            }
        };
    }
}
