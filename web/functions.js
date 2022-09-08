const addSubOrder = () => {
    const nextIndex = $('#subOrdersDiv').children().length;
    const clone = `<fieldset style="width: 15%; margin-bottom: 30px;" id="subOrder${nextIndex}">
        <label>Payer name
            <input type="text" id="payerName${nextIndex}"></input>
        </label><br>
        <label>Currency
                <input type="radio" id="payerCurrency${nextIndex}" value="BRL" checked>Real</input>
            </label><br>
        <div id="productsDiv${nextIndex}">
            Product price <input type="number"></input>                
        </div>
        <button onclick="addProduct(${nextIndex})">Add product</button><br>
    </fieldset>`
    $(`#subOrdersDiv`).append(clone);
}
const addProduct = (index) => {
    const clone = `Product price <input type="number"></input>`;
    $(`#productsDiv${index}`).append(clone);
}

const generatePaymentLinks = () => {
    const orderObject = getOrderObject();
    $.ajax({
        url: 'http://localhost:8080/return-payment-links',
        type: 'GET',
        contentType: 'application/json',
        data: { json: JSON.stringify(orderObject) },
        success(response) {
            const links = response.result;
            links.forEach(link => $('#linksDiv').append(`<a href="${link}" target="_blank">${link}</a><br>`));
        }
    });
}

const getOrderObject = () => {
    const subOrders = getSubOrders();
    return new Object({
        flatTaxes: parseFloat($('#flatTaxes').val()) || 0,
        flatDiscounts: parseFloat($('#flatDiscounts').val()) || 0,
        percentageTaxes: parseFloat($('#percentTaxes').val()) || 0,
        percentageDiscounts: parseFloat($('#percentDiscounts').val()) || 0,
        paymentMethod: $('#paymentMethod').val(),
        subOrders: getSubOrders()
    });
}

const getSubOrders = () => {
    const subOrdersElements = [...$('#subOrdersDiv').children()];
    const subOrders = subOrdersElements.map((el, index) => new Object({
        payerName: $(`#payerName${index}`).val(),
        paymentCurrency: $(`#payerCurrency${index}`).val(),
        products: getProducts(index)
    }));
    return subOrders;
}

const getProducts = (index) => {
    const productsElements = [...$(`#productsDiv${index}`).children('input')];
    return productsElements.filter(numb => numb.value).map(numb => parseFloat(numb.value) || 0);
}