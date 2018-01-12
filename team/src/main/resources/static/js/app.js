// TODO: move the QR stuff to his own module
let numberOfQrsGenerated = 0;
const qrCardTemplate = qrNumber => `
    <div class="row">
      <div class="col-md-6 offset-md-3">
        <div class="card">
          <div class="row">
            <div class="col-md-6">
              <img id="qr-img-${qrNumber}" src="images/default-placeholder.png" class="img-fluid"
                   alt="Generated QR">
            </div>
            <div class="col-md-5 align-self-center">
              <form id="qr-generator-${qrNumber}">
                <div class="form-group row">
                  <label for="qr-foreground-color-${qrNumber}" class="col-md-6 col-form-label">Foreground</label>
                  <div class="col-md-6">
                    <input type="color" class="form-control" id="qr-foreground-color-${qrNumber}" value="#000000">
                  </div>
                </div>
                <div class="form-group row">
                  <label for="qr-background-color-${qrNumber}" class="col-md-6 col-form-label">Background</label>
                  <div class="col-md-6">
                    <input type="color" class="form-control" id="qr-background-color-${qrNumber}" value="#ffffff">
                  </div>
                </div>
                <div class="form-group row">
                  <label for="qr-logo-${qrNumber}" class="col-md-6 col-form-label">Logo</label>
                  <div class="col-md-6">
                    <input type="file" class="form-control" id="qr-logo-${qrNumber}">
                    <input type="hidden" id="qr-logo-file-${qrNumber}" value="">
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
    <br>`;

function qrGeneratorHandler(qrNumber) {
  return (ev) => {
    ev.preventDefault();
    const params = {
      url: $("input[name='url']").val(),
      fg: $(`#qr-foreground-color-${qrNumber}`).val(),
      bg: $(`#qr-background-color-${qrNumber}`).val(),
      id: $(`#qr-logo-file-${qrNumber}`).val() || null
    };
    const strParams = jQuery.param(params);
    const url = `http://localhost:8080/api/qrs?${strParams}`;
    $(`#qr-img-${qrNumber}`).attr('src', url);
  };
}

function logoChangeHandler(qrNumber) {
  return (ev) => {
    ev.preventDefault();
    const formData = new FormData();
    formData.append('logoImg', ev.target.files[0]);
    $.ajax({
      url: 'http://localhost:8080/api/logos',
      method: 'POST',
      contentType: false,
      data: formData,
      dataType: 'json',
      processData: false,
      success(msg) {
        $(`#qr-logo-file-${qrNumber}`).val(msg.id);
      },
    });
  };
}

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
  $('#shortener').submit((event) => {
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

  $('#generate-new-qr').click(() => {
    $('#qr-adder').before(qrCardTemplate(numberOfQrsGenerated));
    $(`#qr-generator-${numberOfQrsGenerated}`).submit(qrGeneratorHandler(numberOfQrsGenerated));
    $(`#qr-logo-${numberOfQrsGenerated}`).change(logoChangeHandler(numberOfQrsGenerated));
    numberOfQrsGenerated += 1;
    if (numberOfQrsGenerated > 2) {
      $('#qr-adder').remove();
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

  $('#stats').submit((event) => {
      event.preventDefault();
      $.ajax({
          url: '/api/stats/' + $(event.currentTarget).serialize(),
          type: 'GET',
          //data: $(event.currentTarget).serialize(),
          success(msg) {
              let rowsCountry = '';
              let rowsBrowser = '';
              let rowsPlatform = '';
              msg.countries.forEach((country) =>
                  rowsCountry += "<td>" + country.data + "</td>"
                      + "<td>" + country.users + "</td>");
              msg.browsers.forEach((browser) =>
                  rowsBrowser += "<td>" + browser.data + "</td>"
                      + "<td>" + browser.users + "</td>");
              msg.platforms.forEach((platform) =>
                  rowsPlatform += "<td>" + platform.data + "</td>"
                      + "<td>" + platform.users + "</td>");
              // language=HTML
              $('#result').html(`
                <div style="background-color:rgba(255, 255, 255, 0.5)" class="container">
                    
                    <div class="panel panel-group text-center">
                        <h2>URL ID Access Stats:</h2>
                        <p>By country, browser and platform of access.</p>
                        <table class="table table-hover text-center">
                            <thead>
                                <tr>
                                    <th>Country</th>
                                    <th>Users</th>
                                </tr>
                             </thead>
                             <tbody>
                                <tr>`
                                + rowsCountry +
                                `</tr>
                            </tbody>
                            <thead>
                                <tr>
                                    <th>Browser</th>
                                    <th>Users</th>
                                </tr>
                             </thead>
                             <tbody>
                                <tr>`
                                + rowsBrowser +
                                `</tr>
                            </tbody>
                            <thead>
                                <tr>
                                    <th>Platform</th>
                                    <th>Users</th>
                                </tr>
                             </thead>
                             <tbody>
                                <tr>`
                                + rowsPlatform +
                                `</tr>
                            </tbody>
                        </table>
                    </div>
                </div>`);
          },
          error() {
              $('#result').html("<div class='alert alert-danger lead'>ERROR</div>");
          },
      });
  });
});
