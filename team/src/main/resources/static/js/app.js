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
    const data = $("input[name='url']").val();
    if (data) {
      const logoFileValue = $(`#qr-logo-file-${qrNumber}`).val();
      const logoId = logoFileValue || null;
      const fgColor = $(`#qr-foreground-color-${qrNumber}`).val();
      const config = {
        bodyColor: fgColor,
        bgColor: $(`#qr-background-color-${qrNumber}`).val(),
        eye1Color: fgColor,
        eye2Color: fgColor,
        eye3Color: fgColor,
        eyeBall1Color: fgColor,
        eyeBall2Color: fgColor,
        eyeBall3Color: fgColor,
        logo: logoId,
      };
      const configStr = JSON.stringify(config);
      const url = `https://qr-generator.qrcode.studio/qr/custom?data=${encodeURIComponent(data)}&config=${encodeURIComponent(configStr)}`;
      $(`#qr-img-${qrNumber}`).attr('src', url);
    }
  };
}

function logoChangeHandler(qrNumber) {
  return (ev) => {
    const formData = new FormData();
    formData.append('file', ev.target.files[0]);
    $.ajax({
      url: 'https://qr-generator.qrcode.studio/qr/uploadimage',
      method: 'POST',
      contentType: false,
      data: formData,
      dataType: 'json',
      processData: false,
      success(msg) {
        $(`#qr-logo-file-${qrNumber}`).val(msg.file);
      },
    });
  };
}

// Get the modal
let modal = document.getElementById('myModal');

$(document).ready(() => {
  $('#shortener').submit((event) => {
    modal.style.display = "block";
    event.preventDefault();
      $.ajax({
      url: '/api/urls',
      type: 'POST',
      data: $(event.currentTarget).serialize(),
      success(msg) {
          modal.style.display = "none";
          if(msg.expirationDate.substring(0, 1) !== "3"){
              $('#result').html(`<div class='alert alert-success lead'><a target='_blank' href='${msg.uri}'>${msg.uri}</a></br><p>The link will expire ${msg.expirationDate} at ${msg.expirationTime}</p></div>`);
          }
          else $('#result').html(`<div class='alert alert-success lead'><a target='_blank' href='${msg.uri}'>${msg.uri}</a></div>`);

      },
      error() {
          modal.style.display = "none";
        $('#result').html("<div class='alert alert-danger lead'>Um... Well... Something didn't go as planned.</br>Maybe try again?</div>");
      },
    });
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
