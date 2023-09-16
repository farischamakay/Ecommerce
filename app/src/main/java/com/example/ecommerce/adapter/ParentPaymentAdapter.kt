import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.adapter.PaymentChildAdapter
import com.example.ecommerce.data.models.response.PaymentItem
import com.example.ecommerce.data.models.response.PaymentType
import com.example.ecommerce.databinding.ItemListParentPaymentBinding

class ParentPaymentAdapter(private val onChildItemClickListener: (PaymentItem) -> Unit) :
    ListAdapter<PaymentType, ParentPaymentAdapter.ParentPaymentViewHolder>(ParentPaymentDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ParentPaymentViewHolder {
        val binding = ItemListParentPaymentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ParentPaymentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParentPaymentViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ParentPaymentViewHolder(val binding: ItemListParentPaymentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val childAdapter = PaymentChildAdapter()

        init {
            childAdapter.setOnItemClickListener { paymentItem ->
                onChildItemClickListener(paymentItem)
            }
        }

        fun bind(data: PaymentType) {
            binding.txtTitleType.text = data.title
            binding.rvPayment.layoutManager = LinearLayoutManager(binding.root.context)
            binding.rvPayment.adapter = childAdapter
            childAdapter.submitList(data.item)
        }
    }
}

private class ParentPaymentDiffUtil : DiffUtil.ItemCallback<PaymentType>() {
    override fun areItemsTheSame(oldItem: PaymentType, newItem: PaymentType): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: PaymentType, newItem: PaymentType): Boolean {
        return oldItem == newItem
    }
}
