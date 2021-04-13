package kz.step.practice.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kz.step.practice.R
import kz.step.practice.adapters.WeatherAdapter
import kz.step.practice.data.Weather
import kz.step.practice.presenter.WeatherInfoPresenter

class EnterFragment : Fragment() {

    lateinit var rootView: View

    var weatherInfoPresenter = WeatherInfoPresenter()
    var weatherInfos: RecyclerView? = null
    var weatherAdapter: WeatherAdapter? = null

    var cityNameEditText: EditText? = null
    var addBtn: Button? = null
    var updBtn: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView =
            LayoutInflater.from(requireContext())
                .inflate(
                    R.layout.fragment_enter,
                    container,
                    false
                )
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        initializeListeners()
        initializeAdapter()
        initializeLinearLayoutManager()

    }

    private fun initializeViews() {
        weatherInfos = rootView.findViewById(R.id.recyclerview_fragment_enter_students)
        cityNameEditText = rootView.findViewById(R.id.edit_text_fragment_enter_city_name)
        addBtn = rootView.findViewById(R.id.button_fragment_enter_add)
        updBtn = rootView.findViewById(R.id.button_fragment_enter_update)
    }

    private fun initializeListeners() {
        addBtn?.setOnClickListener {
            var isExist = false
            val weatherList = getWeatherList()
            val city = cityNameEditText?.text.toString()

            if (weatherList != null) {
                isExist = weatherInfoPresenter.isExist(city)
                for (weather in weatherList) {
                    if (weather.cityName == city) {
                        isExist = true
                        break
                    }
                }
            }
            if (!isExist) {
                var data = Weather().apply {
                    cityName = city
                    temperature = "-"
                    description = "-"
                }
                weatherInfoPresenter.updateWeather(data)
                weatherList?.add(data)
                weatherAdapter = weatherList?.let { it1 -> WeatherAdapter(it1) }
                weatherInfos?.adapter = weatherAdapter

                cityNameEditText?.setText("")
                updateData()
            }
        }
        updBtn?.setOnClickListener{
            updateData()
        }
    }

    private fun updateData() {
        val weatherList = getWeatherList()

        if (weatherList != null) {
            for (data in weatherList) {
                weatherInfoPresenter.updateWeather(data)
            }
        }
    }

    private fun initializeAdapter() {
        var data = Weather().apply {
            cityName = "Nur-Sultan"
            temperature = "-"
            description = "-"
        }
        weatherInfoPresenter.updateWeather(data)
        weatherAdapter = WeatherAdapter(mutableListOf<Weather>(data))
        weatherInfos?.adapter = weatherAdapter
    }

    private fun initializeLinearLayoutManager() {
        weatherInfos?.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getWeatherList(): MutableList<Weather>? {
        return if (weatherAdapter?.itemCount != 0) weatherAdapter?.weatherList else mutableListOf<Weather>()
    }
}