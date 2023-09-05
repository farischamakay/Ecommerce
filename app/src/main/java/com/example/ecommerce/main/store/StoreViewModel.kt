package com.example.ecommerce.main.store


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.ecommerce.data.database.Cart
import com.example.ecommerce.data.models.request.ProductRequest
import com.example.ecommerce.data.models.response.ProductDetailResponse
import com.example.ecommerce.data.models.response.ReviewResponse
import com.example.ecommerce.data.models.response.SearchResponse
import com.example.ecommerce.data.repository.ProductRepository
import com.example.ecommerce.data.repository.RoomCartRepository
import com.example.ecommerce.preferences.PreferenceProvider
import com.example.ecommerce.utils.ResourcesResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val roomCartRepository: RoomCartRepository,
    private val sharedPreferencesManager: PreferenceProvider,
) : ViewModel() {

    private val _param = MutableLiveData<ProductRequest>()
    val param: LiveData<ProductRequest> = _param

    private val _searchResult = MutableLiveData<ResourcesResult<SearchResponse<List<String>>?>>()
    val searchResult: LiveData<ResourcesResult<SearchResponse<List<String>>?>> = _searchResult

    private val _detailProduct = MutableLiveData<ResourcesResult<ProductDetailResponse?>>()
    val detailProduct : LiveData<ResourcesResult<ProductDetailResponse?>> = _detailProduct

    private val _reviewProduct = MutableLiveData<ResourcesResult<ReviewResponse?>>()
    val reviewProduct : LiveData<ResourcesResult<ReviewResponse?>> = _reviewProduct

    private var job: Job? = null

    init {
        setQuery()

    }

    fun setQuery(
        search: String? = null, brand: String? = null, lowest: Int? = null,
        highest: Int? = null, sort: String? = null
    ) {
        _param.postValue(ProductRequest(search, brand, lowest, highest, sort))
    }

    val product =
        param.switchMap {
            productRepository.getProduct(
                it.search,
                it.brand,
                it.lowest,
                it.highest,
                it.sort
            ).cachedIn(viewModelScope)
        }

    fun getUsername(): String? {
        return sharedPreferencesManager.getUsername()
    }

    fun searchItem(query: String) {
        job?.cancel()
        job = viewModelScope.launch {
            delay(800L)
            _searchResult.value = ResourcesResult.Loading
            val result = productRepository.searchProduct(query)
            _searchResult.value = result
        }
    }

    fun detailItem(id: String){
        viewModelScope.launch {
            _detailProduct.value = ResourcesResult.Loading
            val result = productRepository.detailProduct(id)
            _detailProduct.value = result
        }
    }

    fun reviewItem(id: String){
        viewModelScope.launch {
            _reviewProduct.value = ResourcesResult.Loading
            val result = productRepository.reviewProduct(id)
            _reviewProduct.value = result
        }
    }

    val getDataRoom =
        roomCartRepository.fetchCartData()

    fun insertToRoom(cart: Cart){
        viewModelScope.launch {
            roomCartRepository.insertCartData(cart)
        }
    }

    fun updateQuantity(cartList: List<Pair<Cart, Int>>){
        viewModelScope.launch {
            val updates = cartList.map { (cartList, quantity) ->
                cartList.copy(quantity = quantity)
            }
            roomCartRepository.updateValues(updates)
        }
    }
}