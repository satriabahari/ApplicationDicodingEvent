package com.dicoding.applicationdicodingevent.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.dicoding.applicationdicodingevent.data.response.DetailEventResponse
import com.dicoding.applicationdicodingevent.data.response.Event
import com.dicoding.applicationdicodingevent.data.retrofit.ApiConfig
import com.dicoding.applicationdicodingevent.databinding.ActivityDetailEventBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailEventBinding

    companion object {
        private const val TAG = "DetailEventActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        findEventDetail()
    }

    private fun findEventDetail() {
        val eventId = intent.getStringExtra("EVENT_ID")
        showLoading(true)
        if(eventId != null) {
            val client = ApiConfig.getApiService().getDetailEvent(eventId)
            client.enqueue(object : Callback<DetailEventResponse> {
                override fun onResponse(call: Call<DetailEventResponse>, response: Response<DetailEventResponse>) {
                    showLoading(false)
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if(responseBody != null) {
                            setEventData(responseBody.event)
                        }
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }


                override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                    showLoading(false)
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
        }

    }

    private fun setEventData(event: Event?) {
        if (event != null) {
            Glide.with(binding.imgDetail.context)
                .load(event.mediaCover)
                .into(binding.imgDetail)
            binding.tvDetailName.text = event.name
            binding.tvDetailSummary.text = event.summary
            binding.tvDetailOwnerName.text = event.ownerName
            val remainingQuota = (event.quota ?: 0) - (event.registrants ?: 0)
            binding.tvDetailQuota.text = remainingQuota.toString()
            binding.tvDetailBeginTime.text = event.beginTime
            binding.tvDetailDescription.text =
                HtmlCompat.fromHtml(
                    event.description.toString(),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )

            binding.btnDetail.setOnClickListener {
                val url = event.link
                if (!url.isNullOrEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                } else {
                    Log.e(TAG, "Link is null or empty")
                }
            }
        } else {
            Log.e(TAG, "Event data is null")
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}