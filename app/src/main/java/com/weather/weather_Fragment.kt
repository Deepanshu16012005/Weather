package com.weather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.weather.databinding.FragmentWeatherBinding
import retrofit2.Call
import retrofit2.Response

import javax.security.auth.callback.Callback
import kotlin.time.times

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class weather_Fragment : Fragment() {
    private var city: String? = null

    private lateinit var binding: FragmentWeatherBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchBar.setOnClickListener {
            city = binding.searchBar.text.toString()
            getWeatherData(city)
        }}

    private fun getWeatherData(city: String?) {
        val call = ApiBuilder.retrofitBuilder.getCurrentWeather(city ?: "delhi", Constants.API_KEY)

        call.enqueue(object : retrofit2.Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    setViews(response.body())
                } else {
                    Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_LONG).show()
                t.printStackTrace()
            }
        } )
    }



    private fun setViews(response: WeatherResponse?) {
        // Set views with the data from the response
        var humiditytext =response?.main?.humidity.toString()
        binding.humidity.text="$humiditytext%"
        var temptext= response?.main?.temp
        if (temptext != null) {
            binding.temp.text=(temptext-273).toFloat().toString()
        }
        var windspeedtext=response?.wind?.speed?.toInt()
        if (windspeedtext != null) {
            binding.windspeed.text=(windspeedtext*3.6).toString()
        }
        binding.rain.text=response?.rain?.rain1.toString()
    }
}