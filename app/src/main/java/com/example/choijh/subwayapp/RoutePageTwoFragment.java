package com.example.choijh.subwayapp;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.odsay.odsayandroidsdk.API;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RoutePageTwoFragment extends Fragment {

    TextView cost;
    TextView translate;
    TextView time;
    TextView test;
    String start = "201";
    String end = "631";
    // 싱글톤 생성, Key 값을 활용하여 객체 생성




    public RoutePageTwoFragment() {
    }

    public static RoutePageTwoFragment newInstance(int page){
        Bundle args = new Bundle();

        RoutePageTwoFragment fragment = new RoutePageTwoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_route_page_two, container, false);

        cost = (TextView) v.findViewById(R.id.cost2);
        translate = (TextView) v.findViewById(R.id.translate2);
        time = (TextView) v.findViewById(R.id.time2);

        ODsayService odsayService1;
        odsayService1 = ODsayService.init(getActivity().getApplicationContext(), "hdKe/5bDLhm0/zzg6Y2kUqPZy8hL2buzyMpDLGyAH/Y");
        // 서버 연결 제한 시간(단위(초), default : 5초)
        odsayService1.setReadTimeout(5000);
        // 데이터 획득 제한 시간(단위(초), default : 5초)
        odsayService1.setConnectionTimeout(5000);

        OnResultCallbackListener onResultCallbackListener = new OnResultCallbackListener() {
            // 호출 성공 시 실행
            @Override
            public void onSuccess(ODsayData odsayData, API api) {

                try {
                    // API Value 는 API 호출 메소드 명을 따라갑니다.
                    if (api == API.SUBWAY_PATH) {
                        JSONObject result = odsayData.getJson().getJSONObject("result");

                        //총 이동시간 알아오기
                        String travelTime = String.valueOf(result.getInt("globalTravelTime"));
                        time.setText(travelTime + "분");

                        //카드 비용
                        String cardfare = String.valueOf(result.getInt("fare"));
                        cost.setText("카드 " + cardfare + "원");


                        String driveInfoSet = result.getString("driveInfoSet");
                        JSONObject driveInfoJson = new JSONObject(driveInfoSet);

                        JSONArray driveInfo = driveInfoJson.getJSONArray("driveInfo");

                        translate.setText("환승 " + (driveInfo.length()-1) + "회");
                        for (int x = 0; x < driveInfo.length(); x++) {

                            String endName;
                            String startName ;
                            String stationCount;
                            String lane;
                            if (x < (driveInfo.length() - 1)) {
                                JSONObject drive = driveInfo.getJSONObject(x);
                                JSONObject drive1 = driveInfo.getJSONObject(x + 1);
                                endName = drive1.getString("startName");
                                startName = drive.getString("startName");
                                stationCount = String.valueOf(drive.getInt("stationCount"));
                                lane = drive.getString("laneName");
                            }
                            else {
                                JSONObject drive = driveInfo.getJSONObject(x);
                                startName = drive.getString("startName");
                                stationCount = String.valueOf(drive.getInt("stationCount"));
                                lane = drive.getString("laneName");
                                endName = result.getString("globalEndName");

                            }
                            inflateLayout1(lane,startName,endName,stationCount + "개 역");

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            // 호출 실패 시 실행
            @Override
            public void onError(int i, String s, API api) {
                if (api == API.SUBWAY_PATH) {
                }
            }

        };

        // API 호출
        odsayService1.requestSubwayPath("1000", start, end, "2", onResultCallbackListener);


        //new routeTask().execute(start);
        return v;
    }

    public void inflateLayout1(String x, String y, String z, String q) {
        // XML 레이아웃에 정의된 addLayout 객체 참조
        LinearLayout addLayout = (LinearLayout) getActivity().findViewById(R.id.add2);

        // 인플레이션 수행
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if (x.equals("수도권 1호선")) {
            //이것은 addLayout을 부모 컨테이너로 하여 line.xml파일에 정의된 레이아웃을 추가하라는 의미이다.
            inflater.inflate(R.layout.line11, addLayout, true);

            // 새로 추가한 레이아웃 안에 들어있는 객체 참조
            LineView2 lineView = (LineView2) getActivity().findViewById(R.id.lineView11);
            lineView.myPaint.setColor(Color.BLUE);


            TextView arriveStation = (TextView) getActivity().findViewById(R.id.arriveStation11);
            arriveStation.setText(z);

            TextView startStation = (TextView) getActivity().findViewById(R.id.startStation11);
            startStation.setText(y);

            TextView timeLine = (TextView) getActivity().findViewById(R.id.timeLine11);
            timeLine.setText(q);
        } else if (x.equals("수도권 2호선")) {
            inflater.inflate(R.layout.line12, addLayout, true);

            // 새로 추가한 레이아웃 안에 들어있는 객체 참조
            LineView2 lineView = (LineView2) getActivity().findViewById(R.id.lineView12);
            lineView.myPaint.setColor(Color.GREEN);


            TextView arriveStation = (TextView) getActivity().findViewById(R.id.arriveStation12);
            arriveStation.setText(z);

            TextView startStation = (TextView) getActivity().findViewById(R.id.startStation12);
            startStation.setText(y);

            TextView timeLine = (TextView) getActivity().findViewById(R.id.timeLine12);
            timeLine.setText(q);
        } else if (x.equals("수도권 3호선")) {
            inflater.inflate(R.layout.line13, addLayout, true);

            // 새로 추가한 레이아웃 안에 들어있는 객체 참조
            LineView2 lineView = (LineView2) getActivity().findViewById(R.id.lineView13);
            lineView.myPaint.setColor(Color.rgb(255,127,0));



            TextView arriveStation = (TextView) getActivity().findViewById(R.id.arriveStation13);
            arriveStation.setText(z);

            TextView startStation = (TextView) getActivity().findViewById(R.id.startStation13);
            startStation.setText(y);

            TextView timeLine = (TextView) getActivity().findViewById(R.id.timeLine13);
            timeLine.setText(q);
        }
        else if (x.equals("수도권 4호선")) {
            inflater.inflate(R.layout.line14, addLayout, true);

            // 새로 추가한 레이아웃 안에 들어있는 객체 참조
            LineView2 lineView = (LineView2) getActivity().findViewById(R.id.lineView14);
            lineView.myPaint.setColor(Color.rgb(80,188,223));



            TextView arriveStation = (TextView) getActivity().findViewById(R.id.arriveStation14);
            arriveStation.setText(z);

            TextView startStation = (TextView) getActivity().findViewById(R.id.startStation14);
            startStation.setText(y);

            TextView timeLine = (TextView) getActivity().findViewById(R.id.timeLine14);
            timeLine.setText(q);
        }
        else if (x.equals("수도권 5호선")) {
            inflater.inflate(R.layout.line15, addLayout, true);

            // 새로 추가한 레이아웃 안에 들어있는 객체 참조
            LineView2 lineView = (LineView2) getActivity().findViewById(R.id.lineView15);
            lineView.myPaint.setColor(Color.rgb(139,0,255));



            TextView arriveStation = (TextView) getActivity().findViewById(R.id.arriveStation15);
            arriveStation.setText(z);

            TextView startStation = (TextView) getActivity().findViewById(R.id.startStation15);
            startStation.setText(y);

            TextView timeLine = (TextView) getActivity().findViewById(R.id.timeLine15);
            timeLine.setText(q);
        }
        else if (x.equals("수도권 6호선")) {
            inflater.inflate(R.layout.line16, addLayout, true);

            // 새로 추가한 레이아웃 안에 들어있는 객체 참조
            LineView2 lineView = (LineView2) getActivity().findViewById(R.id.lineView16);
            lineView.myPaint.setColor(Color.rgb(150,75,0));



            TextView arriveStation = (TextView) getActivity().findViewById(R.id.arriveStation16);
            arriveStation.setText(z);

            TextView startStation = (TextView) getActivity().findViewById(R.id.startStation16);
            startStation.setText(y);

            TextView timeLine = (TextView) getActivity().findViewById(R.id.timeLine16);
            timeLine.setText(q);
        }
        else if (x.equals("수도권 7호선")) {
            inflater.inflate(R.layout.line17, addLayout, true);

            // 새로 추가한 레이아웃 안에 들어있는 객체 참조
            LineView2 lineView = (LineView2) getActivity().findViewById(R.id.lineView17);
            lineView.myPaint.setColor(Color.rgb(128,128,0));



            TextView arriveStation = (TextView) getActivity().findViewById(R.id.arriveStation17);
            arriveStation.setText(z);

            TextView startStation = (TextView) getActivity().findViewById(R.id.startStation17);
            startStation.setText(y);

            TextView timeLine = (TextView) getActivity().findViewById(R.id.timeLine17);
            timeLine.setText(q);
        }
        else if (x.equals("수도권 8호선")) {
            inflater.inflate(R.layout.line18, addLayout, true);

            // 새로 추가한 레이아웃 안에 들어있는 객체 참조
            LineView2 lineView = (LineView2) getActivity().findViewById(R.id.lineView18);
            lineView.myPaint.setColor(Color.rgb(248,24,148));



            TextView arriveStation = (TextView) getActivity().findViewById(R.id.arriveStation18);
            arriveStation.setText(z);

            TextView startStation = (TextView) getActivity().findViewById(R.id.startStation18);
            startStation.setText(y);

            TextView timeLine = (TextView) getActivity().findViewById(R.id.timeLine18);
            timeLine.setText(q);
        }
        else if (x.equals("수도권 9호선")) {
            inflater.inflate(R.layout.line19, addLayout, true);

            // 새로 추가한 레이아웃 안에 들어있는 객체 참조
            LineView2 lineView = (LineView2) getActivity().findViewById(R.id.lineView19);
            lineView.myPaint.setColor(Color.rgb(198,138,18));



            TextView arriveStation = (TextView) getActivity().findViewById(R.id.arriveStation19);
            arriveStation.setText(z);

            TextView startStation = (TextView) getActivity().findViewById(R.id.startStation19);
            startStation.setText(y);

            TextView timeLine = (TextView) getActivity().findViewById(R.id.timeLine19);
            timeLine.setText(q);
        }
        else{
            inflater.inflate(R.layout.line20, addLayout, true);

            // 새로 추가한 레이아웃 안에 들어있는 객체 참조
            LineView2 lineView = (LineView2) getActivity().findViewById(R.id.lineView20);
            lineView.myPaint.setColor(Color.BLACK);

            TextView line = (TextView) getActivity().findViewById(R.id.line20);
            line.setText(x);

            TextView arriveStation = (TextView) getActivity().findViewById(R.id.arriveStation20);
            arriveStation.setText(z);

            TextView startStation = (TextView) getActivity().findViewById(R.id.startStation20);
            startStation.setText(y);

            TextView timeLine = (TextView) getActivity().findViewById(R.id.timeLine20);
            timeLine.setText(q);
        }


    }

    //백그라운드 작업
    private class routeTask extends AsyncTask<String, Void, String> {
        /** The system calls this to perform work in a worker thread and
         * delivers it the parameters given to AsyncTask.execute()*/

        protected String doInBackground(String... start) {
            /*
            odsayService = ODsayService.init(getActivity().getApplicationContext(), "hdKe/5bDLhm0/zzg6Y2kUqPZy8hL2buzyMpDLGyAH/Y");
            // 서버 연결 제한 시간(단위(초), default : 5초)
            odsayService.setReadTimeout(5000);
            // 데이터 획득 제한 시간(단위(초), default : 5초)
            odsayService.setConnectionTimeout(5000);

            OnResultCallbackListener onResultCallbackListener = new OnResultCallbackListener() {
                // 호출 성공 시 실행
                @Override
                public void onSuccess(ODsayData odsayData, API api) {

                    try {
                        // API Value 는 API 호출 메소드 명을 따라갑니다.
                        if (api == API.SUBWAY_PATH) {
                            JSONObject result = odsayData.getJson().getJSONObject("result");

                            //총 이동시간 알아오기
                            String travelTime = String.valueOf(result.getInt("globalTravelTime"));
                            time.setText(travelTime + "분");

                            //카드 비용
                            String cardfare = String.valueOf(result.getInt("fare"));
                            cost.setText("카드 " + cardfare + "원");


                            String driveInfoSet = result.getString("driveInfoSet");
                            JSONObject driveInfoJson = new JSONObject(driveInfoSet);

                            JSONArray driveInfo = driveInfoJson.getJSONArray("driveInfo");

                            translate.setText("환승 " + (driveInfo.length()-1) + "회");
                            for (int x = 0; x < driveInfo.length(); x++) {

                                String endName;
                                String startName ;
                                String stationCount;
                                String lane;
                                if (x < (driveInfo.length() - 1)) {
                                    JSONObject drive = driveInfo.getJSONObject(x);
                                    JSONObject drive1 = driveInfo.getJSONObject(x + 1);
                                    endName = drive1.getString("startName");
                                    startName = drive.getString("startName");
                                    stationCount = String.valueOf(drive.getInt("stationCount"));
                                    lane = drive.getString("laneName");
                                }
                                else {
                                    JSONObject drive = driveInfo.getJSONObject(x);
                                    startName = drive.getString("startName");
                                    stationCount = String.valueOf(drive.getInt("stationCount"));
                                    lane = drive.getString("laneName");
                                    endName = result.getString("globalEndName");

                                }
                                inflateLayout1(lane,startName,endName,stationCount + "개 역");

                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                // 호출 실패 시 실행
                @Override
                public void onError(int i, String s, API api) {
                    if (api == API.SUBWAY_PATH) {
                    }
                }

            };

            // API 호출
            odsayService.requestSubwayPath("1000", start[0], end, "2", onResultCallbackListener);
            */
            return "SUCCESS";
        }

        /** The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground()*/

        protected void onPostExecute(String value) {
            if(value.equals("SUCCESS")) {

            }
        }

    }
}
