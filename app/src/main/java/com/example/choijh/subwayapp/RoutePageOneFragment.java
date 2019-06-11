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

public class RoutePageOneFragment extends Fragment {

    TextView cost;
    TextView translate;
    TextView time;

    String start = "201";
    String end = "631";
    // 싱글톤 생성, Key 값을 활용하여 객체 생성
    ODsayService odsayService;


    public RoutePageOneFragment() {
    }

    public static RoutePageOneFragment newInstance(int page){
        Bundle args = new Bundle();

        RoutePageOneFragment fragment = new RoutePageOneFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_route_page_one, container, false);

        cost = (TextView) v.findViewById(R.id.cost);
        translate = (TextView) v.findViewById(R.id.translate);
        time = (TextView) v.findViewById(R.id.time);

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

                        translate.setText("환승 " + (driveInfo.length() - 1) + "회");
                        for (int x = 0; x < driveInfo.length(); x++) {

                            String endName;
                            String startName;
                            String stationCount;
                            String lane;
                            if (x < (driveInfo.length() - 1)) {
                                JSONObject drive = driveInfo.getJSONObject(x);
                                JSONObject drive1 = driveInfo.getJSONObject(x + 1);
                                endName = drive1.getString("startName");
                                startName = drive.getString("startName");
                                stationCount = String.valueOf(drive.getInt("stationCount"));
                                lane = drive.getString("laneName");
                            } else {
                                JSONObject drive = driveInfo.getJSONObject(x);
                                startName = drive.getString("startName");
                                stationCount = String.valueOf(drive.getInt("stationCount"));
                                lane = drive.getString("laneName");
                                endName = result.getString("globalEndName");

                            }
                            inflateLayout(lane, startName, endName, stationCount + "개 역");

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

        odsayService.requestSubwayPath("1000", start, end, "1", onResultCallbackListener);

        //new routeTask().execute(start);
        return v;
    }

    public void inflateLayout(String x, String y, String z, String q) {
        // XML 레이아웃에 정의된 addLayout 객체 참조
        LinearLayout addLayout = (LinearLayout) getActivity().findViewById(R.id.add);

        // 인플레이션 수행
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if (x.equals("수도권 1호선")) {
            //이것은 addLayout을 부모 컨테이너로 하여 line.xml파일에 정의된 레이아웃을 추가하라는 의미이다.
            inflater.inflate(R.layout.line, addLayout, true);

            // 새로 추가한 레이아웃 안에 들어있는 객체 참조
            LineView lineView = (LineView) getActivity().findViewById(R.id.lineView);
            lineView.myPaint.setColor(Color.BLUE);


            TextView arriveStation = (TextView) getActivity().findViewById(R.id.arriveStation);
            arriveStation.setText(z);

            TextView startStation = (TextView) getActivity().findViewById(R.id.startStation);
            startStation.setText(y);

            TextView timeLine = (TextView) getActivity().findViewById(R.id.timeLine);
            timeLine.setText(q);
        } else if (x.equals("수도권 2호선")) {
            inflater.inflate(R.layout.line2, addLayout, true);

            // 새로 추가한 레이아웃 안에 들어있는 객체 참조
            LineView lineView = (LineView) getActivity().findViewById(R.id.lineView2);
            lineView.myPaint.setColor(Color.GREEN);


            TextView arriveStation = (TextView) getActivity().findViewById(R.id.arriveStation2);
            arriveStation.setText(z);

            TextView startStation = (TextView) getActivity().findViewById(R.id.startStation2);
            startStation.setText(y);

            TextView timeLine = (TextView) getActivity().findViewById(R.id.timeLine2);
            timeLine.setText(q);
        } else if (x.equals("수도권 3호선")) {
            inflater.inflate(R.layout.line3, addLayout, true);

            // 새로 추가한 레이아웃 안에 들어있는 객체 참조
            LineView lineView = (LineView) getActivity().findViewById(R.id.lineView3);
            lineView.myPaint.setColor(Color.rgb(255,127,0));



            TextView arriveStation = (TextView) getActivity().findViewById(R.id.arriveStation3);
            arriveStation.setText(z);

            TextView startStation = (TextView) getActivity().findViewById(R.id.startStation3);
            startStation.setText(y);

            TextView timeLine = (TextView) getActivity().findViewById(R.id.timeLine3);
            timeLine.setText(q);
        }
        else if (x.equals("수도권 4호선")) {
            inflater.inflate(R.layout.line4, addLayout, true);

            // 새로 추가한 레이아웃 안에 들어있는 객체 참조
            LineView lineView = (LineView) getActivity().findViewById(R.id.lineView4);
            lineView.myPaint.setColor(Color.rgb(80,188,223));



            TextView arriveStation = (TextView) getActivity().findViewById(R.id.arriveStation4);
            arriveStation.setText(z);

            TextView startStation = (TextView) getActivity().findViewById(R.id.startStation4);
            startStation.setText(y);

            TextView timeLine = (TextView) getActivity().findViewById(R.id.timeLine4);
            timeLine.setText(q);
        }
        else if (x.equals("수도권 5호선")) {
            inflater.inflate(R.layout.line5, addLayout, true);

            // 새로 추가한 레이아웃 안에 들어있는 객체 참조
            LineView lineView = (LineView) getActivity().findViewById(R.id.lineView5);
            lineView.myPaint.setColor(Color.rgb(139,0,255));



            TextView arriveStation = (TextView) getActivity().findViewById(R.id.arriveStation5);
            arriveStation.setText(z);

            TextView startStation = (TextView) getActivity().findViewById(R.id.startStation5);
            startStation.setText(y);

            TextView timeLine = (TextView) getActivity().findViewById(R.id.timeLine5);
            timeLine.setText(q);
        }
        else if (x.equals("수도권 6호선")) {
            inflater.inflate(R.layout.line6, addLayout, true);

            // 새로 추가한 레이아웃 안에 들어있는 객체 참조
            LineView lineView = (LineView) getActivity().findViewById(R.id.lineView6);
            lineView.myPaint.setColor(Color.rgb(150,75,0));



            TextView arriveStation = (TextView) getActivity().findViewById(R.id.arriveStation6);
            arriveStation.setText(z);

            TextView startStation = (TextView) getActivity().findViewById(R.id.startStation6);
            startStation.setText(y);

            TextView timeLine = (TextView) getActivity().findViewById(R.id.timeLine6);
            timeLine.setText(q);
        }
        else if (x.equals("수도권 7호선")) {
            inflater.inflate(R.layout.line7, addLayout, true);

            // 새로 추가한 레이아웃 안에 들어있는 객체 참조
            LineView lineView = (LineView) getActivity().findViewById(R.id.lineView7);
            lineView.myPaint.setColor(Color.rgb(128,128,0));



            TextView arriveStation = (TextView) getActivity().findViewById(R.id.arriveStation7);
            arriveStation.setText(z);

            TextView startStation = (TextView) getActivity().findViewById(R.id.startStation7);
            startStation.setText(y);

            TextView timeLine = (TextView) getActivity().findViewById(R.id.timeLine7);
            timeLine.setText(q);
        }
        else if (x.equals("수도권 8호선")) {
            inflater.inflate(R.layout.line8, addLayout, true);

            // 새로 추가한 레이아웃 안에 들어있는 객체 참조
            LineView lineView = (LineView) getActivity().findViewById(R.id.lineView8);
            lineView.myPaint.setColor(Color.rgb(248,24,148));



            TextView arriveStation = (TextView) getActivity().findViewById(R.id.arriveStation8);
            arriveStation.setText(z);

            TextView startStation = (TextView) getActivity().findViewById(R.id.startStation8);
            startStation.setText(y);

            TextView timeLine = (TextView) getActivity().findViewById(R.id.timeLine8);
            timeLine.setText(q);
        }
        else if (x.equals("수도권 9호선")) {
            inflater.inflate(R.layout.line9, addLayout, true);

            // 새로 추가한 레이아웃 안에 들어있는 객체 참조
            LineView lineView = (LineView) getActivity().findViewById(R.id.lineView9);
            lineView.myPaint.setColor(Color.rgb(198,138,18));



            TextView arriveStation = (TextView) getActivity().findViewById(R.id.arriveStation9);
            arriveStation.setText(z);

            TextView startStation = (TextView) getActivity().findViewById(R.id.startStation9);
            startStation.setText(y);

            TextView timeLine = (TextView) getActivity().findViewById(R.id.timeLine9);
            timeLine.setText(q);
        }
        else{
            inflater.inflate(R.layout.line10, addLayout, true);

            // 새로 추가한 레이아웃 안에 들어있는 객체 참조
            LineView lineView = (LineView) getActivity().findViewById(R.id.lineView10);
            lineView.myPaint.setColor(Color.BLACK);

            TextView line = (TextView) getActivity().findViewById(R.id.line10);
            line.setText(x);

            TextView arriveStation = (TextView) getActivity().findViewById(R.id.arriveStation10);
            arriveStation.setText(z);

            TextView startStation = (TextView) getActivity().findViewById(R.id.startStation10);
            startStation.setText(y);

            TextView timeLine = (TextView) getActivity().findViewById(R.id.timeLine10);
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
                                inflateLayout(lane,startName,endName,stationCount + "개 역");

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
            odsayService.requestSubwayPath("1000", start[0], end, "1", onResultCallbackListener);
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
