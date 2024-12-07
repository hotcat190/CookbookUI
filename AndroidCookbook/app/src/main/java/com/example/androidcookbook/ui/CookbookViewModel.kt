package com.example.androidcookbook.ui

import androidx.lifecycle.ViewModel
import com.example.androidcookbook.data.providers.AccessTokenProvider
import com.example.androidcookbook.domain.model.auth.SignInResponse
import com.example.androidcookbook.domain.model.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CookbookViewModel @Inject constructor(
    private val accessTokenProvider: AccessTokenProvider
): ViewModel() {
    private val _uiState = MutableStateFlow(CookbookUiState())
    val uiState: StateFlow<CookbookUiState> = _uiState.asStateFlow()

    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

    fun updateCanNavigateBack(updatedCanNavigateBack: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                canNavigateBack = updatedCanNavigateBack
            )
        }
    }

    fun updateTopBarState(topBarState: CookbookUiState.TopBarState) {
        _uiState.update { it.copy(topBarState = topBarState) }
    }

    fun updateBottomBarState(bottomBarState: CookbookUiState.BottomBarState) {
        _uiState.update { it.copy(bottomBarState = bottomBarState) }
    }

    fun updateUser(response: SignInResponse) {
        _user.update { response.user }
        accessTokenProvider.updateAccessToken(response.accessToken)
    }
}