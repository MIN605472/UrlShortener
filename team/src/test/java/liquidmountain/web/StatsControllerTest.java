package liquidmountain.web;

import liquidmountain.domain.Click;
import liquidmountain.repository.ClickRepository;
import liquidmountain.repository.ShortURLRepository;
import liquidmountain.web.fixture.ClickFixture;
import liquidmountain.web.fixture.ShortURLFixture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class StatsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClickRepository clickRepository;

    @Mock
    private ShortURLRepository shortURLRepository;

    @InjectMocks
    private StatsController statsController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(statsController).build();
    }

    @Test
    public void showStatsWhenNoClicks() throws Exception {

        List<Click> clicks = new ArrayList<>();
        //clicks.add(ClickFixture.click(ShortURLFixture.someUrl()));
        when(shortURLRepository.findByKey("someKey")).thenReturn(ShortURLFixture.someUrl());
        when(clickRepository.findByHash("someKey")).thenReturn(clicks);

        mockMvc.perform(get("/api/stats/{id}", "someKey")).andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(content().json("{'countries':[],'browsers':[],'platforms':[]}"));
    }

    @Test
    public void showStatsWorksIfClicks() throws Exception {

        List<Click> clicks = new ArrayList<>();
        clicks.add(ClickFixture.click(ShortURLFixture.someUrl()));
        when(shortURLRepository.findByKey("someKey")).thenReturn(ShortURLFixture.someUrl());
        when(clickRepository.findByHash("someKey")).thenReturn(clicks);

        mockMvc.perform(get("/api/stats/{id}", "someKey")).andDo(print()).andExpect(status().isAccepted());
    }

}