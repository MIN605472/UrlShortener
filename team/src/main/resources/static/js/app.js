const shortUrl = {
  name: '',
};

const qrs = {
  qrCardTemplate() {
    return `
      <div class="row">
        <div class="col-md-6 offset-md-3">
          <div class="card">
            <div class="row">
              <div class="col-md-6">
                <img id="qr-img-${this.numOfQrsGenerated}" src="images/default-placeholder.png" class="img-fluid"
                     alt="Generated QR">
              </div>
              <div class="col-md-5 align-self-center">
                <form id="qr-generator-${this.numOfQrsGenerated}">
                  <div class="form-group row">
                    <label for="qr-foreground-color-${this.numOfQrsGenerated}" class="col-md-6 col-form-label">Foreground</label>
                    <div class="col-md-6">
                      <input type="color" class="form-control" id="qr-foreground-color-${this.numOfQrsGenerated}" value="#000000">
                    </div>
                  </div>
                  <div class="form-group row">
                    <label for="qr-background-color-${this.numOfQrsGenerated}" class="col-md-6 col-form-label">Background</label>
                    <div class="col-md-6">
                      <input type="color" class="form-control" id="qr-background-color-${this.numOfQrsGenerated}" value="#ffffff">
                    </div>
                  </div>
                  <div class="form-group row">
                    <label for="qr-logo-${this.numOfQrsGenerated}" class="col-md-6 col-form-label">Logo</label>
                    <div class="col-md-6">
                      <input type="file" class="form-control" id="qr-logo-${this.numOfQrsGenerated}">
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-6 offset-md-3">
                      <button type="submit" class="btn btn-primary">Generate</button>
                    </div>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
      <br>
    `;
  },
  qrAdderTemplate() {
    return `
       <div class="row" id="qr-adder">
        <div class="col-md-6 offset-md-3">
          <div class="qr-adder-card">
            <button type="button" class="btn btn-link" id="generate-new-qr" style="opacity: 1">Generate a new QR</button>
          </div>
        </div>
      </div>
    `;
  },
  qrCardHandlers: {
    onSubmit(qrNumber) {
      return function (ev) {
        ev.preventDefault();
        const formData = new FormData();
        if (qrs.qrCards[qrNumber].bg) {
          formData.append('bg', $(`#qr-background-color-${qrNumber}`).val());
        }
        if (qrs.qrCards[qrNumber].fg) {
          formData.append('fg', $(`#qr-foreground-color-${qrNumber}`).val());
        }
        if (qrs.qrCards[qrNumber].logo) {
          formData.append('logoImg', $(`#qr-logo-${qrNumber}`)[0].files[0]);
        }
        $.ajax({
          url: `http://localhost:8080/api/urls/${shortUrl.name}/qrs/${qrNumber}`,
          method: 'PATCH',
          contentType: false,
          data: formData,
          processData: false,
          success() {
            $(`#qr-img-${qrNumber}`).attr('src', `http://localhost:8080/api/urls/${shortUrl.name}/qrs/${qrNumber}?${new Date().getTime()}`);
          },
        });
      };
    },
    onLogoChange(qrNumber) {
      return function (ev) {
        ev.preventDefault();
        qrs.qrCards[qrNumber].logo = true;
      };
    },
    onFgColorChange(qrNumber) {
      return function (ev) {
        ev.preventDefault();
        qrs.qrCards[qrNumber].fg = true;
      };
    },
    onBgColorChange(qrNumber) {
      return function (ev) {
        ev.preventDefault();
        qrs.qrCards[qrNumber].bg = true;
      };
    },
  },
  qrCards: [],
  numOfQrsGenerated: 0,
  maxNumOfQrs: 3,
  currentState: 'empty',
  states: {
    empty: {
      newShortUrl() {
        this.changeStateTo('canAdd');
        $('#qrs').append(this.qrAdderTemplate());
        $('#generate-new-qr').click(() => this.dispatch('addQr'));
      },
    },
    canAdd: {
      addQr() {
        $('#qr-adder').before(this.qrCardTemplate());
        this.qrCards.push({
          fg: false,
          bg: false,
          logo: false,
        });
        $(`#qr-generator-${this.numOfQrsGenerated}`).submit(this.qrCardHandlers.onSubmit(this.numOfQrsGenerated));
        $(`#qr-logo-${this.numOfQrsGenerated}`).change(this.qrCardHandlers.onLogoChange(this.numOfQrsGenerated));
        $(`#qr-foreground-color-${this.numOfQrsGenerated}`).change(this.qrCardHandlers.onFgColorChange(this.numOfQrsGenerated));
        $(`#qr-background-color-${this.numOfQrsGenerated}`).change(this.qrCardHandlers.onBgColorChange(this.numOfQrsGenerated));
        const formData = new FormData();
        formData.append('fg', '#000000');
        formData.append('bg', '#FFFFFF');
        const ajaxOnSuccess = num => jqXHR => $(`#qr-img-${num}`).attr('src', jqXHR.getResponseHeader('Location'));
        const myOnSuccess = ajaxOnSuccess(this.numOfQrsGenerated);
        $.ajax({
          url: `http://localhost:8080/api/urls/${shortUrl.name}/qrs`,
          method: 'POST',
          contentType: false,
          data: formData,
          processData: false,
          success(data, textStatus, jqXHR) {
            myOnSuccess(jqXHR);
          },
        });
        this.numOfQrsGenerated += 1;
        if (this.numOfQrsGenerated >= this.maxNumOfQrs) {
          $('#qr-adder').remove();
          this.changeStateTo('full');
        }
      },
      newShortUrl() {
        this.changeStateTo('canAdd');
        this.numOfQrsGenerated = 0;
        this.qrCards = [];
        $('#qrs').empty();
        $('#qrs').append(this.qrAdderTemplate());
        $('#generate-new-qr').click(() => this.dispatch('addQr'));
      },
    },
    full: {
      removeQr() {
        this.changeStateTo('canAdd');
      },
      newShortUrl() {
        this.changeStateTo('canAdd');
        this.numOfQrsGenerated = 0;
        this.qrCards = [];
        $('#qrs').empty();
        $('#qrs').append(this.qrAdderTemplate());
        $('#generate-new-qr').click(() => this.dispatch('addQr'));
      },
    },
  },
  dispatch(actionName, ...payload) {
    const action = this.states[this.currentState][actionName];
    if (action) {
      action.apply(qrs, ...payload);
    }
  },
  changeStateTo(newState) {
    this.currentState = newState;
  },
};

/**
 * Stop spinners from spinning. Because fuck 2017.
 */
function setSpinners() {
    let safeIconn = document.getElementById('safeIcon');
    let verifyIconn = document.getElementById('verifiedIcon');
    let loadingIconn = document.getElementById('loadingIcon');
    safeIconn.setAttribute('data-icon', 'spinner');
    safeIconn.classList.add('fa-spin');
    verifyIconn.setAttribute('data-icon', 'spinner');
    verifyIconn.classList.add('fa-spin');
    loadingIconn.setAttribute('data-icon', 'spinner');
    loadingIconn.classList.add('fa-spin');
}
// Get the modal
let modal = document.getElementById('myModal');
let safeIcon;
let verifyIcon;
let loadingIcon;
let isSafe = false;
let isVerified = false;

$(document).ready(() => {
    /**
     * Handling the shortener action.
     * Handles ordered petitions to safe, verified and finally shortener.
     * If something fails, returns an error immediately.
     * If everything goes fine, returns the shortened url.
     */
  $('#shortener').submit((event) => {
    isSafe = false;
    isVerified = false;
    modal.style.display = "block";
    event.preventDefault();
      $.ajax({
      url: '/api/safe',
      type: 'POST',
      data: $(event.currentTarget).serialize(),
      success(msg) {
          safeIcon = document.getElementById('safeIcon');
          if(msg === 'SAFE'){
              safeIcon.setAttribute('data-icon', 'check');
              safeIcon.classList.remove('fa-spin');
              isSafe = true;
              $.ajax({
                  url: '/api/verify',
                  type: 'POST',
                  data: $(event.currentTarget).serialize(),
                  success(msg) {
                      verifyIcon = document.getElementById('verifiedIcon');
                      if(msg === 'SAFE') {
                          verifyIcon.setAttribute('data-icon', 'check');
                          verifyIcon.classList.remove('fa-spin');
                          isVerified = true;
                          $.ajax({
                              url: 'api/urls',
                              type: 'POST',
                              data: $(event.currentTarget).serialize(),
                              success(msg) {
                                  loadingIcon = document.getElementById('loadingIcon');
                                  loadingIcon.setAttribute('data-icon', 'check');
                                  loadingIcon.classList.remove('fa-spin');
                                  modal.style.display = "none";
                                  setSpinners();
                                  if(msg.expirationDate.substring(0, 1) !== "3"){
                                      $('#result').html(`<div class='alert alert-success lead'><a target='_blank' href='${msg.uri}'>${msg.uri}</a></br><p>The link will expire ${msg.expirationDate} at ${msg.expirationTime}</p></div>`);
                                  }
                                  else $('#result').html(`<div class='alert alert-success lead'><a target='_blank' href='${msg.uri}'>${msg.uri}</a></div>`);
                                  shortUrl.name = msg.uri.substr(msg.uri.lastIndexOf('/') + 1);
                                  qrs.dispatch('newShortUrl');
                              },
                              error() {
                                  setSpinners();
                                  modal.style.display = "none";
                                  $('#result').html("<div class='alert alert-danger lead'>Um... Well... Something didn't go as planned.</br>Maybe try again?</div>");
                              }
                          });
                      } else{
                          setSpinners();
                          modal.style.display = "none";
                          $('#result').html("<div class='alert alert-danger lead'>Check your grammar, bro.</br>That URL is not correctly formed.</div>");
                      }
                  },
                  error() {
                      setSpinners();
                      modal.style.display = "none";
                      $('#result').html("<div class='alert alert-danger lead'>Um... Well... Something didn't go as planned.</br>Maybe try again?</div>");
                  }
              });
          }
          else {
              setSpinners();
              modal.style.display = "none";
              $('#result').html("<div class='alert alert-danger lead'>Ok, stop right there!</br>Your URL seems insecure, no way we're shortening that.</div>");
          }

      },
      error() {
          setSpinners();
          modal.style.display = "none";
        $('#result').html("<div class='alert alert-danger lead'>Um... Well... Something didn't go as planned.</br>Maybe try again?</div>");
      },
    });

      if(isSafe && isVerified) {


      }

  });


  $('#goBackButton').click(() => {
      window.location = "index.html"
    });

    $('#statsButton').click(() => {
        window.location = "stats.html"
    });

    $('#indexButton').click(() => {
        window.location = "index.html"
    });

    /**
     * Sets the data for the Country chart
     * @param data: Array containing the necessary data formatted correctly for ChartJS
     */
    function setCountryData(data) {
        let countryChartJS = document.getElementById("countryChartJS");
        let countryChart = new Chart(countryChartJS, {
            type: 'doughnut',
            data: data,
            options: {
                title: {
                    display: true,
                    position: 'top',
                    text: 'Countries'
                },
                legend: {
                    display: false
                },
                responsive: true,
                maintainAspectRatio: true,
                animation: {
                    animateRotate: true,
                    animateScale: true
                }
            }
        });
    }

    /**
     * Sets the data for the Browser chart
     * @param data: Array containing the necessary data formatted correctly for ChartJS
     */
    function setBrowserData(data) {
        let browserChartJS = document.getElementById("browserChartJS");
        let browserChart = new Chart(browserChartJS, {
            type: 'doughnut',
            data: data,
            options: {
                title: {
                    display: true,
                    position: 'top',
                    text: 'Browsers'
                },
                legend: {
                    display: false
                },
                responsive: true,
                maintainAspectRatio: true,
                animation: {
                    animateRotate: true,
                    animateScale: true
                }
            }
        });
    }

    /**
     * Sets the data for the Platform chart
     * @param data: Array containing the necessary data formatted correctly for ChartJS
     */
    function setPlatformData(data) {
        let platformChartJS = document.getElementById("platformChartJS");
        let platformChart = new Chart(platformChartJS, {
            type: 'doughnut',
            data: data,
            options: {
                title: {
                    display: true,
                    position: 'top',
                    text: 'Platforms'
                },
                legend: {
                    display: false
                },
                responsive: true,
                maintainAspectRatio: true,
                animation: {
                    animateRotate: true,
                    animateScale: true
                }
            }
        });
    }

    /**
     * Handles the show stats event.
     * Handles the petition and displays the stat charts.
     * If something goes wrong, displays error.
     * If everything goes fine, displays the stat charts.
     */
  $('#stats').submit((event) => {
      event.preventDefault();
      $.ajax({
          url: '/api/stats/' + $(event.currentTarget).serialize(),
          type: 'GET',
          //data: $(event.currentTarget).serialize(),
          success(msg) {
              console.log(msg);
              let countryData = {
                  datasets: [{
                      backgroundColor: "rgba(255,156,74,0.4)",
                      borderColor: "rgba(255,156,74,1)",
                      data: []
                  }],
                  // These labels appear in the legend and in the tooltips when hovering different arcs
                  labels: []
              };
              let browserData = {
                  datasets: [{
                      backgroundColor: 'rgba(16,157,255,0.4)',
                      borderColor: "rgba(16,157,255,1)",
                      data: []
                  }],
                  // These labels appear in the legend and in the tooltips when hovering different arcs
                  labels: []
              };
              let platformData = {
                  datasets: [{
                      backgroundColor: 'rgba(16,221,159,0.4)',
                      borderColor: "rgba(16,221,159,1)",
                      data: []
                  }],
                  // These labels appear in the legend and in the tooltips when hovering different arcs
                  labels: []
              };
              msg.countries.forEach((country) => {
                      countryData.datasets[0].data.push(country.users);
                      countryData.labels.push(country.data);
              });
              msg.browsers.forEach((browser) => {
                  browserData.datasets[0].data.push(browser.users);
                  browserData.labels.push(browser.data);
              });
              msg.platforms.forEach((platform) => {
                  platformData.datasets[0].data.push(platform.users);
                  platformData.labels.push(platform.data);
              });
              $('#result').html(`
                    <div style="background-color:rgba(255, 255, 255, 0.5)" class="container">
                        <div class="panel panel-group text-center">
                            <h2>URL ID Access Stats:</h2>
                            <div class="chart-container col-lg-12">
                                <canvas id="countryChartJS"></canvas>
                            </div>
                            <div class="chart-container col-lg-12">
                                <canvas id="browserChartJS"></canvas>
                            </div>
                            <div class="chart-container col-lg-12">
                                <canvas id="platformChartJS"></canvas>
                            </div>
                        </div>
                    </div>`);

              console.log(countryData);
              console.log(browserData);
              console.log(platformData);
              setCountryData(countryData);
              setBrowserData(browserData);
              setPlatformData(platformData);
          },
          error() {
              $('#result').html("<div class='alert alert-danger lead'>ERROR</div>");
          },
      });
  });
});
