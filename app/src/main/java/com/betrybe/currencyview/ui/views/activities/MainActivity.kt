package com.betrybe.currencyview.ui.views.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.betrybe.currencyview.R
import com.betrybe.currencyview.common.ApiIdlingResource
import com.betrybe.currencyview.data.api.ApiService
import com.betrybe.currencyview.data.api.RetrofitClient
import com.betrybe.currencyview.data.models.CurrencySymbolResponse
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private val mCurrencyInputLayout: TextInputLayout by lazy { findViewById(R.id.currency_selection_input_container) }
    private val mCurrencyMaterialAutoComplete: MaterialAutoCompleteTextView by lazy { findViewById(R.id.currency_selection_input_layout) }
    private val mCurrencyRatesState: RecyclerView by lazy { findViewById(R.id.currency_rates_state) }

    private val loadCurrencyState: MaterialTextView by lazy { findViewById(R.id.load_currency_state) }
    private val selectCurrencyState: MaterialTextView by lazy { findViewById(R.id.select_currency_state) }
    private val waitingResponseState: View by lazy { findViewById(R.id.waiting_response_state) }
    private val currencyRatesState: RecyclerView by lazy { findViewById(R.id.currency_rates_state) }

    private var currencies: List<String>? = null

    private lateinit var adapter: ArrayAdapter<String>


    private fun setupAutoCompleteAdapter() {
        // Inicialize o adaptador
        adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line)

        // Configure o AutoCompleteTextView com o adaptador
        mCurrencyMaterialAutoComplete.setAdapter(adapter)
    }
    private fun loadCurrenciesFromApi() {
        // instância do serviço Retrofit
        val apiService = RetrofitClient.instance

        // corrotinas para realizar a chamada de forma assíncrona
        CoroutineScope(Dispatchers.IO).launch {
            try {
                ApiIdlingResource.increment()

                val response: Response<CurrencySymbolResponse> = apiService.getCurrentSymbols()

                // ver se a chamada foi bem sucedida
                if (response.isSuccessful) {
                    // listar as moedas
                    val currencyResponse: CurrencySymbolResponse? = response.body()
                    currencies = currencyResponse?.symbols?.keys?.toList()
                    Log.d("CurrencyApp", "Chamada à API bem-sucedida")
                    runOnUiThread {
                        val currenciesList = currencies
                        if (currenciesList != null) {
                            Log.d("CurrencyApp", "Moedas recebidas: $currencies")
                            adapter.clear()
                            adapter.addAll(currenciesList)
                            adapter.notifyDataSetChanged()

                        }
                        loadCurrencyState.visibility = View.GONE
                        waitingResponseState.visibility = View.GONE
                        currencyRatesState.visibility = View.GONE
                        selectCurrencyState.visibility = View.VISIBLE
                    }
                } else {
                    // Tratar erro na resposta da API
                    Log.e("CurrencyApp", "Erro na resposta da API: ${response.code()}")
                }
                // ADICIONAR ESSA LINHA
                ApiIdlingResource.decrement()
            } catch (e: HttpException) {
                // ADICIONAR ESSA LINHA
                ApiIdlingResource.decrement()

                Log.e("CurrencyApp", "Erro durante a chamada à API", e)
            } catch (e: IOException) {
                // ADICIONAR ESSA LINHA
                ApiIdlingResource.decrement()

                //...
                // Seu Codigo de erro de IOException
                // ...
            }
        }
    }

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Configurar o adaptador para o AutoCompleteTextView
        setupAutoCompleteAdapter()
        loadCurrenciesFromApi()
        val layoutManager = LinearLayoutManager(this)
        currencyRatesState.layoutManager = layoutManager
    }
}
