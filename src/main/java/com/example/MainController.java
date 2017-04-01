package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@Controller
public class MainController {
    @ResponseBody
    @RequestMapping(value = "/employee", method = RequestMethod.GET)
    public String home(Model model) throws IOException{
        ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");

        EmployeeJDBCTemplate employeeJDBCTemplate = (EmployeeJDBCTemplate)context.getBean("employeeJDBCTemplate");

        List<Employee> employees = employeeJDBCTemplate.listEmployees();
        model.addAttribute("employees", employees);
        String result = "<h1>Töötajate nimekiri</h1><ul>";
        Integer currDepth = 0;

        for (Employee s: employees) {
            if(s.getDepth() > currDepth){
                result += "<ul>";
            }
            if(s.getDepth() < currDepth){
                currDepth = currDepth - s.getDepth();
                String message = String.join("", Collections.nCopies(currDepth, "</ul>"));
                result += message;
            }
            result += "<li>"+ s.getName() + "</li>";
            currDepth = s.getDepth();
        }
        result += "</ul>";
        return result;
    }

    @ResponseBody
    @RequestMapping("/add")
    public String add(@RequestParam(value = "name") String name, @RequestParam(value = "boss") String boss){
        ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
        EmployeeJDBCTemplate employeeJDBCTemplate = (EmployeeJDBCTemplate)context.getBean("employeeJDBCTemplate");
        employeeJDBCTemplate.create(name, boss);
        return "Employee added";
    }

    @RequestMapping(value = "/calculate", method = RequestMethod.GET)
    public void doGet(@RequestParam(value = "num1", required=false, defaultValue = "0") float num1, @RequestParam(value="num2", required=false, defaultValue = "0") float num2, @RequestParam(value= "op", required=false, defaultValue = "sum") String op, Model model){
        float vastus = 0;

        if(op.equals("sum")){
            vastus = num1 + num2;
        }else if(op.equals("sub")){
            vastus = num1 - num2;
        }else if(op.equals("prod")) {
            vastus = num1 * num2;
        }else if(op.equals("div")){
            vastus = num1 / num2;
        }
        model.addAttribute("vastus", vastus);
    }
    @ResponseBody
    @RequestMapping(path = "/calculate", method = RequestMethod.POST)
    public float doPost(@RequestBody String json, Model model) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> nums;
        nums = mapper.readValue(json, HashMap.class);

        String num1String = nums.get("num1");
        String num2String = nums.get("num2");
        String op = nums.get("op");

        float num1 = Float.valueOf(num1String);
        float num2 = Float.valueOf(num2String);
        float vastus2 = 0;

        if(op.equals("sum")){
            vastus2 = num1 + num2;
        }else if(op.equals("sub")){
            vastus2 = num1 - num2;
        }else if(op.equals("prod")) {
            vastus2 = num1 * num2;
        }else if(op.equals("div")){
            vastus2 = num1 / num2;
        }
        return vastus2;
    }

    @RequestMapping("/hello")
    public String hello(Model model){
        model.addAttribute("message", "Hello World!");
        return "hello";
    }
}
