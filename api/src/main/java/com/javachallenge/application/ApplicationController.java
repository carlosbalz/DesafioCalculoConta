package com.javachallenge.application;


import java.text.DecimalFormat;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {

    @RequestMapping("/return-values")    
    public String returnPaymentUrl() {
        Double[][] input = new Double[][]{
            new Double[]{ 40d, 2d }, 
            new Double[]{ 8d }
        }; 
        Double[] valores = calculaValorPorPessoa(input, 20d, 0d, 8d, 0d);
        StringBuilder resultString = new StringBuilder();
        for(int i = 0; i < valores.length; i++) {
            resultString.append(new DecimalFormat("##.##").format(valores[i]) + " ");
        }

        return resultString.toString();
    }

    private Double[] calculaValorPorPessoa(Double[][] pedido, Double valorDesconto, Double porcentagemDesconto, Double valorTaxas, Double porcentagemTaxas) {
        Double valorTotalDoPedido = 0d;
        Double[] valorTotalPorPessoa = new Double[pedido.length];                
        Double[] valoresFinais = new Double[pedido.length]        ;

        //calcula o valor total de cada pedido sem descontos e taxas
        for(int i = 0; i < pedido.length; i++) {
            
            Double valorTotalDaPessoa = 0d;

            for(int x = 0; x < pedido[i].length; x++) {
                valorTotalDaPessoa += pedido[i][x];
            }
            valorTotalDoPedido += valorTotalDaPessoa;
            valorTotalPorPessoa[i] = valorTotalDaPessoa;
        }   
        
        //calcula o valor dos descontos e taxas para cada pedido
        for(int i = 0; i < valorTotalPorPessoa.length; i++) {
            Double porcentagemDoTotal = valorTotalPorPessoa[i] / valorTotalDoPedido;
            Double valoresDescontosETaxas = 0d;

            if(valorDesconto > 0) {                
                valoresDescontosETaxas -= valorDesconto * porcentagemDoTotal;
            }            

            if(porcentagemDesconto > 0) {
                valoresDescontosETaxas -= valorTotalPorPessoa[i] * porcentagemDesconto;
            }

            if(valorTaxas > 0) {
                valoresDescontosETaxas += valorTaxas * porcentagemDoTotal;
            }

            if(porcentagemTaxas > 0) {
                valoresDescontosETaxas += valorTotalPorPessoa[i] * porcentagemDesconto;
            }

            valoresFinais[i] = valorTotalPorPessoa[i] + valoresDescontosETaxas;
        }        
        return valoresFinais;     
    }    
}
