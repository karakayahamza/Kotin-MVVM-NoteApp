package com.example.noteapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.ui.listeners.NoteClickListener
import com.example.noteapp.ui.adapter.NoteAdapter
import com.example.noteapp.databinding.FragmentMainScreenBinding
import com.example.noteapp.model.NoteModel
import com.example.noteapp.util.CustomAlertDialog
import com.example.noteapp.viewModel.NoteViewModel

class MainScreen : Fragment() {

    private lateinit var binding: FragmentMainScreenBinding
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteModelArrayList: ArrayList<NoteModel>
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var note: List<NoteModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        return binding.root

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupRecyclerView()
        setupAddButton()
        setupNoteItemClickListeners()
        //setupNoteItemLongClickListeners()

        binding.searchView.clearFocus()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })

        /*val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, // Sürükleme işlemine izin verilen yönler
            ItemTouchHelper.LEFT // Sadece sola kaydırmaya izin veriyoruz
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val sourcePosition = viewHolder.adapterPosition
                val targetPosition = target.adapterPosition

                // Öğelerin sırasını değiştir
                Collections.swap(noteModelArrayList, sourcePosition, targetPosition)

                // Adapter'a sıra değişikliğini bildir
                noteAdapter.notifyItemMoved(sourcePosition, targetPosition)

                // Sürükleme işlemi sonrasında geri gitmesini önlemek için false döndürün
                return false
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)


                val myScope = CoroutineScope(Dispatchers.Main)

                // Asenkron bir işlemi başlatmak için Coroutine'u kullanın
                myScope.launch {
                    try {
                        // Arka planda çalışacak işlemi başlatın
                        val result = withContext(Dispatchers.IO) {
                            //noteViewModel.updatePosition(noteModelArrayList)
                            "Succes"
                        }

                        // İşlem tamamlandığında ana ekranda sonucu işleyebilirsiniz
                        handleResult(result)
                    } catch (e: Exception) {
                        // Hata durumunda hata işleme kodunu buraya ekleyin
                        println("Hata bulundu")
                    }
                }

                for (i in noteModelArrayList){
                    println(i.title)
                }
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Kaydırma işlemi gerçekleştiğinde çalışan metod
                if (direction == ItemTouchHelper.LEFT) {
                    val position = viewHolder.adapterPosition

                    // Özel bir diyalog penceresi göstererek öğeyi silme işlemi
                    val customDialog = CustomAlertDialog(requireContext()) {
                        val noteToDelete = noteModelArrayList[position]
                        noteViewModel.delete(noteToDelete)
                        noteModelArrayList.removeAt(position)
                        noteAdapter.notifyItemRemoved(position)
                        checkRecyclerView(noteModelArrayList)
                        noteAdapter.notifyItemRangeChanged(position, noteModelArrayList.size)
                    }
                    customDialog.showDialog("Bu notu silmek istiyor musunuz?")
                }
            }

            override fun getSwipeDirs(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                // Swipe işlemine izin vermek istediğiniz yönlere göre ayarlayabilirsiniz.
                // Sadece sola kaydırmaya izin veriyoruz.
                return ItemTouchHelper.LEFT
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.recycylerViewNotes)*/


    }



    @SuppressLint("NotifyDataSetChanged")
    private fun filterList(newText: String?) {
        val filteredList = ArrayList<NoteModel>()
        for (item in note){
            if (item.title.lowercase().contains(newText!!.lowercase())){
                filteredList.add(item)
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(requireContext(),"Note bulunamadı.",Toast.LENGTH_SHORT).show()
        }
        else{
            noteAdapter.setFilteredList(filteredList)
            noteAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupUI() {
        noteModelArrayList = ArrayList()
        noteAdapter = NoteAdapter(noteModelArrayList)
        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        noteViewModel.allNotes.observe(viewLifecycleOwner) { notes ->
            note = notes
            noteModelArrayList.apply {
                clear()
                addAll(notes)
                checkRecyclerView(noteModelArrayList)
            }
            noteAdapter.notifyDataSetChanged()
        }
    }

    private fun setupRecyclerView() {
        binding.recycylerViewNotes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = noteAdapter
        }
    }

    private fun setupAddButton() {
        binding.addNewNote.setOnClickListener {
            val action = MainScreenDirections.actionMainScreenToNoteEditScreen(null)
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun setupNoteItemClickListeners() {
        noteAdapter.setOnNoteClickListener(object : NoteClickListener {
            override fun onNoteClickListener(position: Int) {
                val action = MainScreenDirections.actionMainScreenToNoteEditScreen(noteModelArrayList[position])
                Navigation.findNavController(binding.root).navigate(action)
            }
        })
    }

    private fun setupNoteItemLongClickListeners() {
        noteAdapter.setOnNoteLongClickListener(object : NoteClickListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onNoteClickListener(position: Int) {
                val customDialog = CustomAlertDialog(requireContext()){
                    val noteToDelete = noteModelArrayList[position]
                    noteViewModel.delete(noteToDelete)
                    noteModelArrayList.removeAt(position)
                    noteAdapter.notifyItemRemoved(position)
                    checkRecyclerView(noteModelArrayList)
                }
                customDialog.showDialog("Bu notu silmek istiyor musunuz?")
            }
        })
    }

    fun checkRecyclerView(notes : ArrayList<NoteModel>){
        if (notes.size==0){
            binding.noNotes.visibility = View.VISIBLE
        }
        else{
            binding.noNotes.visibility = View.GONE
        }
    }
}
