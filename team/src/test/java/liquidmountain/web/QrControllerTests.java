package liquidmountain.web;

import liquidmountain.repository.QrRepository;
import liquidmountain.repository.ShortURLRepository;
import liquidmountain.web.fixture.QrFixture;
import liquidmountain.web.fixture.ShortURLFixture;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class QrControllerTests {
    private MockMvc mockMvc;

    @Mock
    private QrRepository qrRepository;

    @Mock
    private ShortURLRepository shortURLRepository;

    @InjectMocks
    private QrController qrController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(qrController).build();
    }

    @Test
    public void thatItCreatesANewQr() {
        try {
            doReturn(QrFixture.qrWithoutLogo()).when(qrRepository).retrieveQr("1", 0);
            doReturn(ShortURLFixture.anotherUrlWithValidHash()).when(shortURLRepository).findByKey("1");
            MockMultipartFile bgPart = new MockMultipartFile("bg", "", MediaType.TEXT_PLAIN_VALUE, QrFixture
                    .qrWithoutLogo().getBg().getHexColor().getBytes());
            MockMultipartFile fgPart = new MockMultipartFile("fg", "", MediaType.TEXT_PLAIN_VALUE, QrFixture
                    .qrWithoutLogo().getFg().getHexColor().getBytes());
            mockMvc.perform(fileUpload("/api/urls/" + ShortURLFixture.anotherUrlWithValidHash().getHash() + "/qrs")
                    .file(bgPart).file(fgPart)).andDo(print()).andExpect(status().isCreated()).andExpect(header()
                    .string("Location", "http://localhost:8080/api/urls/" + ShortURLFixture.anotherUrlWithValidHash()
                            .getHash() + "/qrs/0"));
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void thatItGetAQr() {
        try {
            doReturn(QrFixture.qrWithoutLogo()).when(qrRepository).retrieveQr("1", 0);
            mockMvc.perform(get("/api/urls/" + ShortURLFixture.anotherUrlWithValidHash().getHash() + "/qrs/0")).andDo
                    (print()).andExpect(status().isOk()).andExpect(header().string("Content-Type", MediaType
                    .IMAGE_PNG_VALUE));
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void thatItPatchesAQr() {
        try {
            doNothing().when(qrRepository).update("1", 0, QrFixture.qrWithoutLogo());
            MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.fileUpload("/api/urls/" +
                    ShortURLFixture.anotherUrlWithValidHash().getHash() + "/qrs/0");
            builder.with(request -> {
                request.setMethod("PATCH");
                return request;
            });
            MockMultipartFile bgPart = new MockMultipartFile("bg", "", MediaType.TEXT_PLAIN_VALUE, QrFixture
                    .qrWithoutLogo().getBg().getHexColor().getBytes());
            MockMultipartFile fgPart = new MockMultipartFile("fg", "", MediaType.TEXT_PLAIN_VALUE, QrFixture
                    .qrWithoutLogo().getFg().getHexColor().getBytes());
            mockMvc.perform(builder.file(bgPart).file(fgPart)).andDo(print()).andExpect(status().isNoContent());
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
