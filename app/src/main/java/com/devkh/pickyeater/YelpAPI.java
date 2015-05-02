package com.devkh.pickyeater;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
 * Created by kevin on 5/2/15.
 * Yelp API
 */
public class YelpAPI {

    private static final String API_HOST = "api.yelp.com";
    private static final String DEFAULT_TERM = "Food";
    private static final String DEFAULT_LOCATION = "San Jose";
    private static final int SEARCH_LIMIT = 3; // limit for # of results
    private static final String SEARCH_PATH = "/v2/search";
    // NOT NEEDED - private static final String BUSINESS_PATH = "/v2/business";

    /*
     * Update OAuth credentials below from the Yelp Developers API site:
     * http://www.yelp.com/developers/getting_started/api_access
     */
    private static final String CONSUMER_KEY = "l54pqZdfx4s7hJMzIenopQ";
    private static final String CONSUMER_SECRET = "chJ5RiY78NgbziiHelwT4D-RAlY";
    private static final String TOKEN = "uMWEnAnqr9h-njWY1kQ7gUZN2kkrtweA";
    private static final String TOKEN_SECRET = "zJ6BU4MfUThF8Mq7k_MjTFvlL5Y";

    OAuthService service;
    Token accessToken;

    /**
     * Setup the Yelp API OAuth credentials.
     *
     * @param consumerKey    Consumer key
     * @param consumerSecret Consumer secret
     * @param token          Token
     * @param tokenSecret    Token secret
     */
    public YelpAPI(String consumerKey, String consumerSecret, String token, String tokenSecret) {
        this.service =
                new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(consumerKey)
                        .apiSecret(consumerSecret).build();
        this.accessToken = new Token(token, tokenSecret);
    }

    /**
     * Creates and sends a request to the Search API by term and location.
     * <p>
     * See <a href="http://www.yelp.com/developers/documentation/v2/search_api">Yelp Search API V2</a>
     * for more info.
     *
     * @param term <tt>String</tt> of the search term to be queried
     * @param location <tt>String</tt> of the location
     * @return <tt>String</tt> JSON Response
     */
    public String searchByFood(String term, String location) {
        OAuthRequest request = createOAuthRequest(SEARCH_PATH);
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("location", location);
        request.addQuerystringParameter("limit", String.valueOf(SEARCH_LIMIT));
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
        return response.getBody();
    }

//    /**
//     * Queries the Search API based on the command line arguments and takes the first result to query
//     * the Business API.
//     *
//     * @param yelpApi <tt>YelpAPI</tt> service instance
//     * @param yelpApiCli <tt>YelpAPICLI</tt> command line arguments
//     */
//    private static void queryAPI(YelpAPI yelpApi, YelpAPICLI yelpApiCli) {
//        String searchResponseJSON =
//                yelpApi.searchForBusinessesByLocation(yelpApiCli.term, yelpApiCli.location);
//
//        JSONParser parser = new JSONParser();
//        JSONObject response = null;
//        try {
//            response = (JSONObject) parser.parse(searchResponseJSON);
//        } catch (ParseException pe) {
//            System.out.println("Error: could not parse JSON response:");
//            System.out.println(searchResponseJSON);
//            System.exit(1);
//        }
//
//        JSONArray businesses = (JSONArray) response.get("businesses");
//        JSONObject firstBusiness = (JSONObject) businesses.get(0);
//        String firstBusinessID = firstBusiness.get("id").toString();
//        System.out.println(String.format(
//                "%s businesses found, querying business info for the top result \"%s\" ...",
//                businesses.size(), firstBusinessID));
//
//        // Select the first business and display business details
//        String businessResponseJSON = yelpApi.searchByBusinessId(firstBusinessID.toString());
//        System.out.println(String.format("Result for business \"%s\" found:", firstBusinessID));
//        System.out.println(businessResponseJSON);
//    }
}
