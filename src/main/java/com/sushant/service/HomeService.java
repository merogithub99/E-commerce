package com.sushant.service;

import com.sushant.model.Home;
import com.sushant.model.HomeCategory;

import java.util.List;

public interface HomeService {
    public Home createHomePageData(List<HomeCategory> allCategories);

}
