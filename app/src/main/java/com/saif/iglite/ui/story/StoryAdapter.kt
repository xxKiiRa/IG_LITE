import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saif.iglite.databinding.ItemStoryBinding
import com.saif.iglite.model.Story

class StoryAdapter(private val items: List<Story>) :
    RecyclerView.Adapter<StoryAdapter.VH>() {

    inner class VH(val b: ItemStoryBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val it = items[position]
        with(holder.b) {
            tvName.text = it.name
            ivAvatar.setImageResource(it.avatarRes)
        }
    }
}