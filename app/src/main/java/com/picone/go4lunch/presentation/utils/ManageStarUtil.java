package com.picone.go4lunch.presentation.utils;

import android.view.View;

import androidx.annotation.NonNull;

import com.picone.go4lunch.databinding.StarLayoutBinding;

public class ManageStarUtil {

    public static void manageStar(@NonNull StarLayoutBinding starLayoutBinding, int averageSatisfaction) {
        switch (averageSatisfaction) {
            case 0:
                starLayoutBinding.opinionStar1.setVisibility(View.GONE);
                starLayoutBinding.opinionStar2.setVisibility(View.GONE);
                starLayoutBinding.opinionStar3.setVisibility(View.GONE);
                break;
            case 1:
                starLayoutBinding.opinionStar1.setVisibility(View.VISIBLE);
                starLayoutBinding.opinionStar2.setVisibility(View.GONE);
                starLayoutBinding.opinionStar3.setVisibility(View.GONE);
                break;
            case 2:
                starLayoutBinding.opinionStar1.setVisibility(View.VISIBLE);
                starLayoutBinding.opinionStar2.setVisibility(View.VISIBLE);
                starLayoutBinding.opinionStar3.setVisibility(View.GONE);
                break;
            case 3:
                starLayoutBinding.opinionStar1.setVisibility(View.VISIBLE);
                starLayoutBinding.opinionStar2.setVisibility(View.VISIBLE);
                starLayoutBinding.opinionStar3.setVisibility(View.VISIBLE);
        }
    }
}
