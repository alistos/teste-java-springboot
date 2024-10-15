package org.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    @ResponseBody
    public String home() {
        return "<h1>Welcome to the home page!</h1>" +
                "<ul>" +
                "<li><a href='/api/processos'>Get all Processos</a></li>" +
                "</ul>" +
                "<form id='processoForm' onsubmit='submitForm(event)'>" +
                "  <label for='processoId'>Enter Processo ID:</label><br>" +
                "  <input type='text' id='processoId' name='processoId'><br><br>" +
                "  <input type='submit' value='Get Processo by ID'>" +
                "</form>" +
                "<script>" +
                "  function submitForm(event) {" +
                "    event.preventDefault();" +
                "    var id = document.getElementById('processoId').value;" +
                "    window.location.href = '/api/processos/' + id;" +
                "  }" +
                "</script>";
    }
}