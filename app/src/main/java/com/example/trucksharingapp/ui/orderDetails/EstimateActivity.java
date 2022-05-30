package com.example.trucksharingapp.ui.orderDetails;

import static com.amap.api.services.route.RouteSearch.DRIVING_SINGLE_DEFAULT;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.example.trucksharingapp.R;
import com.example.trucksharingapp.model.OrderModel;
import com.example.trucksharingapp.overlay.AMapUtil;
import com.example.trucksharingapp.overlay.DrivingRouteOverlay;
import com.example.trucksharingapp.payment.Json;
import com.example.trucksharingapp.payment.PaymentsUtil;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Optional;


public class EstimateActivity extends AppCompatActivity implements RouteSearch.OnRouteSearchListener {

    private MapView mMapView;
    private RouteSearch routeSearch;
    private LatLonPoint mStartPoint;//起点，116.335891,39.942295
    private LatLonPoint mEndPoint;//终点，116.481288,39.995576
    private AMap aMap;
    private DriveRouteResult mDriveRouteResult;
    private ImageView backButton;
    private TextView titleTextView;
    private TextView rightButton;
    private OrderModel data;
    private BasePopupView loadingDialog;
    private TextView startTextView;
    private TextView endTextView;
    private TextView infoString;
    private Button bookNow;
    private Button callDriver;
    private double priceDouble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate);


        initView(savedInstanceState);
        initData();
        initPayment();
    }


    private void initData() {
        data = (OrderModel) getIntent().getSerializableExtra("data");
        loadingDialog = new XPopup.Builder(this)
                .asLoading("Loading...")
                .show();
        getStartAndEndPoint(data.getLocation(), false);
        startTextView.setText("Pickup location: " + data.getLocation());
        endTextView.setText("Drop-off location: " + data.getDropOffLocation());
    }


    private void initView(Bundle savedInstanceState) {
        mMapView = (MapView) findViewById(R.id.mMapView);
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();


        backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        titleTextView.setText("Order details");
        rightButton = (TextView) findViewById(R.id.rightButton);
        startTextView = (TextView) findViewById(R.id.startTextView);
        endTextView = (TextView) findViewById(R.id.endTextView);
        infoString = (TextView) findViewById(R.id.infoString);
        bookNow = (Button) findViewById(R.id.bookNow);
        googlePayButton = bookNow;
        callDriver = (Button) findViewById(R.id.callDriver);
        callDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:4008007878"));
                startActivity(intent);
            }
        });
        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPayment(view);
            }
        });
    }


    private void getStartAndEndPoint(String location, boolean isEnd) {
        try {
            GeocodeSearch geocodeSearch = new GeocodeSearch(this);
            geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                @Override
                public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

                }

                @Override
                public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

                    if (i == 1000) {
                        if (geocodeResult != null && geocodeResult.getGeocodeAddressList() != null &&
                                geocodeResult.getGeocodeAddressList().size() > 0) {

                            GeocodeAddress geocodeAddress = geocodeResult.getGeocodeAddressList().get(0);
                            double latitude = geocodeAddress.getLatLonPoint().getLatitude();//纬度
                            double longititude = geocodeAddress.getLatLonPoint().getLongitude();//经度
                            String adcode = geocodeAddress.getAdcode();//区域编码
                            if (isEnd) {
                                mEndPoint = new LatLonPoint(latitude, longititude);
                                try {
                                    routeSearch = new RouteSearch(EstimateActivity.this);
                                    routeSearch.setRouteSearchListener(EstimateActivity.this);
                                    Log.e("准备开始获取路线", "准备开始获取路线: mStartPoint" + mStartPoint.toString() + "===" + mEndPoint.toString());
                                    final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                                            mStartPoint, mEndPoint);
                                    RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, DRIVING_SINGLE_DEFAULT, null,
                                            null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
                                    routeSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
                                } catch (AMapException e) {
                                    e.printStackTrace();
                                    Log.e("TAG", "initData: " + e.getMessage());
                                }
                            } else {
                                mStartPoint = new LatLonPoint(latitude, longititude);
                                getStartAndEndPoint(data.getDropOffLocation(), true);
                            }


                        } else {
                            Toast.makeText(EstimateActivity.this, "地名出错", Toast.LENGTH_SHORT).show();
//                        ToastUtils.show(context,"地址名出错");
                        }
                    }
                }
            });

            GeocodeQuery geocodeQuery = new GeocodeQuery(location, "29");
            geocodeSearch.getFromLocationNameAsyn(geocodeQuery);


        } catch (AMapException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);

                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                            this, aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")";
                    int taxiCost = (int) mDriveRouteResult.getTaxiCost();
                    Log.e("TAG", "onDriveRouteSearched: " + "打车约" + taxiCost + "元" + "\n" + des);
                    infoString.setText("Approx. Fare: $" + AMapUtil.getFriendlyLength(dis) + "\nApprox. travel time: " + AMapUtil.getFriendlyTime(dur));
                    priceDouble = Double.parseDouble(AMapUtil.getFriendlyLength(dis));
                } else if (result != null && result.getPaths() == null) {
                    Toast.makeText(this, "Failed to load address", Toast.LENGTH_SHORT).show();

                }

            } else {
                Toast.makeText(this, "Failed to load address", Toast.LENGTH_SHORT).show();
            }

        }
        loadingDialog.dismiss();

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;
    private static final long SHIPPING_COST_CENTS = 90 * PaymentsUtil.CENTS_IN_A_UNIT.longValue();
    private PaymentsClient paymentsClient;
    private Button googlePayButton;
    private JSONArray garmentList;
    private JSONObject selectedGarment;

    private void initPayment() {
        try {
            selectedGarment = fetchRandomGarment();
            displayGarment(selectedGarment);
        } catch (JSONException e) {
            throw new RuntimeException("The list of garments cannot be loaded");
        }

        // Initialize a Google Pay API client for an environment suitable for testing.
        // It's recommended to create the PaymentsClient object inside of the onCreate method.
        paymentsClient = PaymentsUtil.createPaymentsClient(this);
        possiblyShowGooglePayButton();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // value passed in AutoResolveHelper
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                switch (resultCode) {

                    case Activity.RESULT_OK:
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        handlePaymentSuccess(paymentData);
                        break;

                    case Activity.RESULT_CANCELED:
                        // The user cancelled the payment attempt
                        break;

                    case AutoResolveHelper.RESULT_ERROR:
                        Status status = AutoResolveHelper.getStatusFromIntent(data);
                        handleError(status.getStatusCode());
                        break;
                }

                // Re-enables the Google Pay payment button.
                googlePayButton.setClickable(true);
        }
    }

    private void displayGarment(JSONObject garment) throws JSONException {
        final String escapedHtmlText = Html.fromHtml(
                garment.getString("description"), Html.FROM_HTML_MODE_COMPACT).toString();
        Log.e("TAG", "detailTitle: " + garment.getString("title"));
        Log.e("TAG", "detailPrice: " + String.format(Locale.getDefault(), "$%.2f", garment.getDouble("price")));
        Log.e("TAG", "detailTitle: " + Html.fromHtml(
                escapedHtmlText, Html.FROM_HTML_MODE_COMPACT));


//        layoutBinding.detailDescription.setText(Html.fromHtml(
//                escapedHtmlText, Html.FROM_HTML_MODE_COMPACT));

//        final String imageUri = String.format("@drawable/%s", garment.getString("image"));
//        final int imageResource = getResources().getIdentifier(imageUri, null, getPackageName());
//        layoutBinding.detailImage.setImageResource(imageResource);
    }

    private void possiblyShowGooglePayButton() {

        final Optional<JSONObject> isReadyToPayJson = PaymentsUtil.getIsReadyToPayRequest();
        if (!isReadyToPayJson.isPresent()) {
            return;
        }

        // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
        // OnCompleteListener to be triggered when the result of the call is known.
        IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(isReadyToPayJson.get().toString());
        Task<Boolean> task = paymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(this,
                new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            setGooglePayAvailable(task.getResult());
                        } else {
                            Log.w("isReadyToPay failed", task.getException());
                        }
                    }
                });
    }

    private void setGooglePayAvailable(boolean available) {
        if (available) {
            googlePayButton.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, available + "===", Toast.LENGTH_LONG).show();
        }
    }

    private void handlePaymentSuccess(PaymentData paymentData) {

        // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
        Log.e("TAG", "handlePaymentSuccess: "+"====1");

        final String paymentInfo = paymentData.toJson();
        if (paymentInfo == null) {
            return;
        }
        Log.e("TAG", "handlePaymentSuccess: "+"====2");
        try {
            Log.e("TAG", "handlePaymentSuccess: "+"====3");

            JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");
            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".
            Log.e("TAG", "handlePaymentSuccess: "+"====4");

            final JSONObject tokenizationData = paymentMethodData.getJSONObject("tokenizationData");
            Log.e("TAG", "handlePaymentSuccess: "+"====5");

            final String token = tokenizationData.getString("token");
            Log.e("TAG", "handlePaymentSuccess: "+"====6");

            final JSONObject info = paymentMethodData.getJSONObject("info");
            Log.e("TAG", "handlePaymentSuccess: "+"====7");

            final String billingName = info.getJSONObject("billingAddress").getString("name");
            Log.e("TAG", "handlePaymentSuccess: "+"====");
            Toast.makeText(
                    this, "Payment succeeded with " + billingName + " account",
                    Toast.LENGTH_LONG).show();
            // Logging token string.
            Log.d("Google Pay token: ", token);

        } catch (JSONException e) {
            throw new RuntimeException("The selected garment cannot be parsed from the list of elements");
        }
    }

    private void handleError(int statusCode) {
        Log.e("loadPaymentData failed", String.format("Error code: %d", statusCode));
    }

    public void requestPayment(View view) {

        // Disables the button to prevent multiple clicks.
        googlePayButton.setClickable(false);

        // The price provided to the API should include taxes and shipping.
        // This price is not displayed to the user.

        double garmentPrice = 0.01;
        long garmentPriceCents = Math.round(garmentPrice * PaymentsUtil.CENTS_IN_A_UNIT.longValue());
        long priceCents = garmentPriceCents + SHIPPING_COST_CENTS;

        Optional<JSONObject> paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(priceCents);
        if (!paymentDataRequestJson.isPresent()) {
            return;
        }

        PaymentDataRequest request =
                PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());

        // Since loadPaymentData may show the UI asking the user to select a payment method, we use
        // AutoResolveHelper to wait for the user interacting with it. Once completed,
        // onActivityResult will be called with the result.
        if (request != null) {
            AutoResolveHelper.resolveTask(
                    paymentsClient.loadPaymentData(request),
                    this, LOAD_PAYMENT_DATA_REQUEST_CODE);
        }


    }

    private JSONObject fetchRandomGarment() {

        // Only load the list of items if it has not been loaded before
        if (garmentList == null) {
            garmentList = Json.readFromResources(this, R.raw.tshirts);
        }

        // Take a random element from the list
        int randomIndex = Math.toIntExact(Math.round(Math.random() * (garmentList.length() - 1)));
        try {
            return garmentList.getJSONObject(randomIndex);
        } catch (JSONException e) {
            throw new RuntimeException("The index specified is out of bounds.");
        }
    }
}