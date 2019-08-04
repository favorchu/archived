package competition.onedata.android.hongming.listview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import competition.onedata.android.hongming.NotificationActivity;
import competition.onedata.android.hongming.WeatherDetailsActivity;
import competition.onedata.android.map.R;

public class WeatherItem extends Item {

	private Double temperature = 0.0;
	private Double humidity = 0.0;
	private Double uvIndex = 0.0;
	private Double generalAirQualityFrom = 0.0;
	private Double generalAirQualityTo = 0.0;
	private Double roadSideAirQuality = 0.0;

	public WeatherItem(Activity context) {
		super(context);
	}

	@Override
	public int getViewType() {
		return VIEW_WEATHER;
	}

	@Override
	protected void refreshViewContent(View convertView) {
		TextView textTemperature = (TextView) convertView
				.findViewById(R.id.temperature);

		Typeface fontLight = Typeface.createFromAsset(getContext().getAssets(),
				"Roboto-Thin.ttf");
		Typeface fontThin = Typeface.createFromAsset(getContext().getAssets(),
				"Roboto-Thin.ttf");

		textTemperature.setTypeface(fontLight);

		textTemperature.setText(temperature + "°C");

		TextView textHumidity = (TextView) convertView
				.findViewById(R.id.humidityMessage);
		textHumidity.setTypeface(fontThin);
		textHumidity.setText(Math.round(humidity) + "%");

		TextView textUVIndex = (TextView) convertView
				.findViewById(R.id.uvIndexMessage);
		textUVIndex.setTypeface(fontThin);
		textUVIndex.setText(String.valueOf(Math.round(uvIndex)));

		TextView textAirQuality = (TextView) convertView
				.findViewById(R.id.airQualityMessage);
		textAirQuality.setTypeface(fontThin);
		textAirQuality.setText(Math.round(generalAirQualityFrom) + "-"
				+ Math.round(generalAirQualityTo));

		TextView textRoadAirQuality = (TextView) convertView
				.findViewById(R.id.roadSideAirQualityMessage);
		textRoadAirQuality.setTypeface(fontThin);
		textRoadAirQuality.setText(String.valueOf(Math
				.round(roadSideAirQuality)));

	}

	@Override
	protected View getLayout(LayoutInflater inflater) {
		View row = inflater.inflate(R.layout.weather_layout, null);
		// row.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// }
		// });
		//
		return row;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public Double getHumidity() {
		return humidity;
	}

	public void setHumidity(Double humidity) {
		this.humidity = humidity;
	}

	public Double getUvIndex() {
		return uvIndex;
	}

	public void setUvIndex(Double uvIndex) {
		this.uvIndex = uvIndex;
	}

	public Double getGeneralAirQualityFrom() {
		return generalAirQualityFrom;
	}

	public void setGeneralAirQualityFrom(Double generalAirQualityFrom) {
		this.generalAirQualityFrom = generalAirQualityFrom;
	}

	public Double getGeneralAirQualityTo() {
		return generalAirQualityTo;
	}

	public void setGeneralAirQualityTo(Double generalAirQualityTo) {
		this.generalAirQualityTo = generalAirQualityTo;
	}

	public Double getRoadSideAirQuality() {
		return roadSideAirQuality;
	}

	public void setRoadSideAirQuality(Double roadSideAirQuality) {
		this.roadSideAirQuality = roadSideAirQuality;
	}

	@Override
	public void onItemClick() {
		Log.i(this.getClass().getSimpleName(),
				"notification view I am clicked.");

		Intent intent = new Intent(getContext(), WeatherDetailsActivity.class);
		Bundle b = new Bundle();
		// b.putString("uv", getTitle());
		// b.putString("date", getMessageDate());
		// b.putString("message", getMessage());
		intent.putExtras(b); // Put your id to your next Intent
		getContext().startActivity(intent);
	}

}
