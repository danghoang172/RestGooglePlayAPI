//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.google.play.rest;

import com.akdeniz.googleplaycrawler.GooglePlayAPI;
import com.akdeniz.googleplaycrawler.GooglePlay.AggregateRating;
import com.akdeniz.googleplaycrawler.GooglePlay.AppDetails;
import com.akdeniz.googleplaycrawler.GooglePlay.DetailsResponse;
import com.akdeniz.googleplaycrawler.GooglePlay.DocV2;
import com.akdeniz.googleplaycrawler.GooglePlay.Image;
import com.akdeniz.googleplaycrawler.GooglePlay.Offer;
import com.akdeniz.googleplaycrawler.GooglePlay.SearchResponse;
import com.google.play.model.App;
import com.google.play.model.PlayStorePage;
import com.google.play.model.Rating;
import com.google.play.model.Token;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

class PlayStore {
    private GooglePlayAPI api;
    private int EXPRIRE_TIME = 3600;

    PlayStore(GooglePlayAPI api) {
        this.api = api;
    }

    public GooglePlayAPI getApi() {
        return this.api;
    }

    List<App> searchApp(String packageName) throws IOException {
        ArrayList pageList = new ArrayList();
        SearchResponse response = this.api.search(packageName);
        Iterator i$ = response.getDoc(0).getChildList().iterator();

        while(i$.hasNext()) {
            DocV2 doc = (DocV2)i$.next();
            this.getApp(doc);
            pageList.add(this.getApp(doc));
        }

        return pageList;
    }

    PlayStorePage getAppDetail(String packageName) throws IOException {
        DetailsResponse details = this.api.details(packageName);
        List imageGGList = details.getDocV2().getImageList();
        LinkedHashMap imageList = new LinkedHashMap();
        int x = 1;

        for(Iterator rating = imageGGList.iterator(); rating.hasNext(); ++x) {
            Image basic = (Image)rating.next();
            imageList.put(Integer.valueOf(x), basic.getImageUrl());
        }

        Rating var9 = this.getRating(details);
        App var10 = this.getApp(details.getDocV2());
        PlayStorePage appPage = new PlayStorePage(var9, imageList, var10);
        return appPage;
    }

    private App getApp(DocV2 doc) {
        String title = doc.getTitle();
        String creator = doc.getCreator();
        List categories = doc.getDetails().getAppDetails().getAppCategoryList();
        String descriptionHTML = doc.getDescriptionHtml();
        App appBasic = new App(title, creator, categories, descriptionHTML, doc.getAggregateRating().getRatingsCount());
        return appBasic;
    }

    private Rating getRating(DetailsResponse details) {
        AggregateRating rateList = details.getDocV2().getAggregateRating();
        long star5 = rateList.getFiveStarRatings();
        long star4 = rateList.getFourStarRatings();
        long star3 = rateList.getThreeStarRatings();
        long star2 = rateList.getTwoStarRatings();
        long star1 = rateList.getOneStarRatings();
        float averageRating = rateList.getStarRating();
        Rating appRating = new Rating(star5, star4, star3, star2, star1, averageRating);
        return appRating;
    }

    public Token getToken() {
        String token = this.api.getToken();
        return token == null?null:new Token(token, this.EXPRIRE_TIME);
    }

    InputStream downloadApp(String packageName) throws IOException {
        DetailsResponse details = this.api.details(packageName);
        AppDetails appDetails = details.getDocV2().getDetails().getAppDetails();
        Offer offer = details.getDocV2().getOffer(0);
        int versionCode = appDetails.getVersionCode();
        int offerType = offer.getOfferType();
        long installationSize = appDetails.getInstallationSize();
        System.out.println("Downloading to server..." + packageName + " : " + installationSize + " bytes");
        InputStream downloadStream = this.api.download(packageName, versionCode, offerType);
        return downloadStream;
    }
}
