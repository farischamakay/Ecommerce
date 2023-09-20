package com.example.ecommerce.main.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.adapter.NotificationAdapter
import com.example.ecommerce.data.database.notification.Notification
import com.example.ecommerce.databinding.FragmentNotificationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val viewModel : NotificationViewModel by viewModels()
    private lateinit var notificationAdapter: NotificationAdapter
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

        notificationAdapter.setOnItemClickCallback(object : NotificationAdapter.OnItemClickCallback{
            override fun onItemClicked(notification: Notification) {
                viewModel.updateNotification(notification)
            }

        })

        viewModel.getDataNotification.observe(viewLifecycleOwner){ response ->
            notificationAdapter.submitList(response)

        }

    }

}