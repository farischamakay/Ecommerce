package com.example.ecommerce.main.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.adapter.NotificationAdapter
import com.example.ecommerce.core.data.database.notification.Notification
import com.example.ecommerce.databinding.FragmentNotificationBinding
import com.example.ecommerce.utils.Constants
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val viewModel: NotificationViewModel by viewModels()
    private lateinit var notificationAdapter: NotificationAdapter

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        notificationAdapter = NotificationAdapter()
        binding.rvNotification.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotification.adapter = notificationAdapter

        notificationAdapter.setOnItemClickCallback(object :
            NotificationAdapter.OnItemClickCallback {
            override fun onItemClicked(notification: Notification) {
                firebaseAnalytics.logEvent(Constants.BUTTON_CLICK) {
                    param(Constants.BUTTON_NAME, "btn_notification")
                }
                viewModel.updateNotification(notification)
            }

        })

        viewModel.getDataNotification.observe(viewLifecycleOwner) { response ->
            binding.notificationError.root.isVisible = response.isEmpty()
            notificationAdapter.submitList(response)

        }

    }

}