import com.denisgasparoto.minderset.domain.model.FlashCard
import com.denisgasparoto.minderset.domain.model.ValidationError
import com.denisgasparoto.minderset.domain.usecase.AddFlashCardUseCase
import com.denisgasparoto.minderset.domain.usecase.DeleteFlashCardUseCase
import com.denisgasparoto.minderset.domain.usecase.GetFlashCardsUseCase
import com.denisgasparoto.minderset.domain.usecase.ValidateFlashCardUseCase
import com.denisgasparoto.minderset.presentation.FlashCardViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FlashCardViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getFlashCardsUseCase: GetFlashCardsUseCase
    private lateinit var addFlashCardUseCase: AddFlashCardUseCase
    private lateinit var deleteFlashCardUseCase: DeleteFlashCardUseCase
    private lateinit var validateFlashCardUseCase: ValidateFlashCardUseCase

    private val sampleCards = listOf(
        FlashCard(id = 1, question = "Q1", answer = "A1", category = "cat1"),
        FlashCard(id = 2, question = "Q2", answer = "A2", category = "cat2"),
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        getFlashCardsUseCase = mockk()
        addFlashCardUseCase = mockk()
        deleteFlashCardUseCase = mockk()
        validateFlashCardUseCase = ValidateFlashCardUseCase()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial loadCards success updates uiState with cards`() = runTest {
        coEvery { getFlashCardsUseCase() } returns sampleCards

        val vm = FlashCardViewModel(
            getFlashCardsUseCase,
            addFlashCardUseCase,
            deleteFlashCardUseCase,
            validateFlashCardUseCase,
            testDispatcher
        )

        advanceUntilIdle()

        val state = vm.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals(sampleCards, state.cards)
    }

    @Test
    fun `initial loadCards failure updates uiState with error`() = runTest {
        val errorMsg = "Failed to load"
        coEvery { getFlashCardsUseCase() } throws Exception(errorMsg)

        val vm = FlashCardViewModel(
            getFlashCardsUseCase,
            addFlashCardUseCase,
            deleteFlashCardUseCase,
            validateFlashCardUseCase,
            testDispatcher
        )

        advanceUntilIdle()

        val state = vm.uiState.value
        assertFalse(state.isLoading)
        assertEquals(errorMsg, state.errorMessage)
        assertTrue(state.cards.isEmpty())
    }

    @Test
    fun `addCard success reloads cards`() = runTest {
        val newCard = FlashCard(id = 3, question = "Q3", answer = "A3", category = "")

        coEvery { addFlashCardUseCase(any()) } returns Unit
        coEvery { getFlashCardsUseCase() } returns sampleCards + newCard

        val vm = FlashCardViewModel(
            getFlashCardsUseCase,
            addFlashCardUseCase,
            deleteFlashCardUseCase,
            validateFlashCardUseCase,
            testDispatcher
        )

        advanceUntilIdle()

        vm.addCard(newCard.question, newCard.answer, newCard.category)
        advanceUntilIdle()

        val state = vm.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertTrue(state.cards.any { it.question == newCard.question && it.answer == newCard.answer })
    }

    @Test
    fun `addCard failure updates uiState with error`() = runTest {
        val newCard = FlashCard(id = 3, question = "Q3", answer = "A3", category = "")
        val errorMsg = "Add failed"

        coEvery { addFlashCardUseCase(any()) } throws Exception(errorMsg)
        coEvery { getFlashCardsUseCase() } returns sampleCards

        val vm = FlashCardViewModel(
            getFlashCardsUseCase,
            addFlashCardUseCase,
            deleteFlashCardUseCase,
            validateFlashCardUseCase,
            testDispatcher
        )

        advanceUntilIdle()

        vm.addCard(newCard.question, newCard.answer, newCard.category)
        advanceUntilIdle()

        val state = vm.uiState.value
        assertFalse(state.isLoading)
        assertEquals(errorMsg, state.errorMessage)
    }

    @Test
    fun `deleteCard success reloads cards`() = runTest {
        val cardToDelete = sampleCards[0]

        coEvery { deleteFlashCardUseCase(cardToDelete) } returns Unit
        coEvery { getFlashCardsUseCase() } returns sampleCards - cardToDelete

        val vm = FlashCardViewModel(
            getFlashCardsUseCase,
            addFlashCardUseCase,
            deleteFlashCardUseCase,
            validateFlashCardUseCase,
            testDispatcher
        )

        advanceUntilIdle()

        vm.deleteCard(cardToDelete)
        advanceUntilIdle()

        val state = vm.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertFalse(state.cards.contains(cardToDelete))
    }

    @Test
    fun `deleteCard failure updates uiState with error`() = runTest {
        val cardToDelete = sampleCards[0]
        val errorMsg = "Delete failed"

        coEvery { deleteFlashCardUseCase(cardToDelete) } throws Exception(errorMsg)
        coEvery { getFlashCardsUseCase() } returns sampleCards

        val vm = FlashCardViewModel(
            getFlashCardsUseCase,
            addFlashCardUseCase,
            deleteFlashCardUseCase,
            validateFlashCardUseCase,
            testDispatcher
        )

        advanceUntilIdle()

        vm.deleteCard(cardToDelete)
        advanceUntilIdle()

        val state = vm.uiState.value
        assertFalse(state.isLoading)
        assertEquals(errorMsg, state.errorMessage)
    }

    @Test
    fun `validateCard returns errors when invalid`() {
        val vm = FlashCardViewModel(
            getFlashCardsUseCase,
            addFlashCardUseCase,
            deleteFlashCardUseCase,
            validateFlashCardUseCase,
            testDispatcher
        )

        val shortQuestion = "Q"
        val longQuestion = "Q".repeat(40)
        val shortAnswer = "A"
        val longAnswer = "A".repeat(201)

        var errors = vm.validateCard(shortQuestion, "Valid answer")
        assertTrue(errors.containsKey("question"))
        assertEquals(ValidationError.QuestionTooShort, errors["question"])

        errors = vm.validateCard(longQuestion, "Valid answer")
        assertTrue(errors.containsKey("question"))
        assertEquals(ValidationError.QuestionTooLong, errors["question"])

        errors = vm.validateCard("Valid question", shortAnswer)
        assertTrue(errors.containsKey("answer"))
        assertEquals(ValidationError.AnswerTooShort, errors["answer"])

        errors = vm.validateCard("Valid question", longAnswer)
        assertTrue(errors.containsKey("answer"))
        assertEquals(ValidationError.AnswerTooLong, errors["answer"])
    }

    @Test
    fun `validateCard returns empty map when valid`() {
        val vm = FlashCardViewModel(
            getFlashCardsUseCase,
            addFlashCardUseCase,
            deleteFlashCardUseCase,
            validateFlashCardUseCase,
            testDispatcher
        )

        val errors = vm.validateCard("Valid Q", "Valid Answer")
        assertTrue(errors.isEmpty())
    }
}
