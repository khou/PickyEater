package com.devkh.pickyeater;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.util.Random;

/**
 * Created by Kevin & Benton on 5/2/15.
 * Yelp API
 */
public class YelpAPI {

    private static final String API_HOST = "api.yelp.com";
    private static final String DEFAULT_TERM = "Restaurants";
    private static final String DEFAULT_LOCATION = "San Jose";
    private static final int SEARCH_LIMIT = 20;
    private static final String SEARCH_PATH = "/v2/search";
    private static final String BUSINESS_PATH = "/v2/business";
    // Offset the list of returned business results by this amount (ALWAYS RANDOMIZED)
    private static int SEARCH_OFFSET;

    OAuthService service;
    Token accessToken;

    /**
     * Setup the Yelp API OAuth credentials.
     */
    public YelpAPI() {
        /*
         * Create a YelpCredentials class with your own Yelp API Keys and generate getters
         */
        YelpCredentials yc = new YelpCredentials();
        this.service =
                new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(yc.getCONSUMER_KEY())
                        .apiSecret(yc.getCONSUMER_SECRET()).build();
        this.accessToken = new Token(yc.getTOKEN(), yc.getTOKEN_SECRET());
    }

    /**
     * Custom search API call for PickyEater/PYDF
     *
     * @param terms    <tt>String</tt> delimited by commas of the types of food the user inputted
     * @param location <tt>String</tt> of the user's selected location
     * @return <tt>String</tt> JSON Response of the search
     */
    public String searchForFoodByTerm(String terms, String location, String radius) {

        // Random generator = new Random();
        // SEARCH_OFFSET = generator.nextInt(50);

        OAuthRequest request = createOAuthRequest(SEARCH_PATH);
        request.addQuerystringParameter("term", terms);
        request.addQuerystringParameter("location", location);
        request.addQuerystringParameter("limit", String.valueOf(SEARCH_LIMIT));
        // request.addQuerystringParameter("offset", String.valueOf(SEARCH_OFFSET));
        // request.addQuerystringParameter("radius_filter", radius);
        return sendRequestAndGetResponse(request);
    }

    /**
     * Creates and sends a request to the Business API by business ID.
     * <p/>
     * See <a href="http://www.yelp.com/developers/documentation/v2/business">Yelp Business API V2</a>
     * for more info.
     *
     * @param businessID <tt>String</tt> business ID of the requested business
     * @return <tt>String</tt> JSON Response
     */
    public String searchByBusinessId(String businessID) {
        OAuthRequest request = createOAuthRequest(BUSINESS_PATH + "/" + businessID);
        return sendRequestAndGetResponse(request);
    }

    /**
     * Creates and returns an {@link OAuthRequest} based on the API endpoint specified.
     *
     * @param path API endpoint to be queried
     * @return <tt>OAuthRequest</tt>
     */
    private OAuthRequest createOAuthRequest(String path) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://" + API_HOST + path);
        return request;
    }

    /**
     * Sends an {@link OAuthRequest} and returns the {@link Response} body.
     *
     * @param request {@link OAuthRequest} corresponding to the API request
     * @return <tt>String</tt> body of API response
     */
    private String sendRequestAndGetResponse(OAuthRequest request) {
        System.out.println("Querying " + request.getCompleteUrl() + " ...");
        this.service.signRequest(this.accessToken, request);
        Response response = request.send();
        // System.out.println(response.getBody());
        return response.getBody();
    }
}
