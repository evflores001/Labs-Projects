//
//  ViewController.swift
//  AsyncUSDConverter
//
//  Created by Enrique Flores on 7/9/20.
//  Copyright © 2020 Enrique Flores. All rights reserved.
//

import UIKit
import Foundation

struct currencyRates: Decodable{
    //Needed to decode JSON response
    let base: String
    let date: String
    let time_last_updated: Int
    let rates: Dictionary<String,Double>
}

class ViewController: UIViewController, UIPickerViewDataSource, UIPickerViewDelegate {
   
    
    @IBOutlet weak var textfield1: UITextField!
    
    @IBOutlet weak var pickerView1: UIPickerView!
    
    @IBOutlet weak var myLabel1: UILabel!
    
    
    var pickerData = ["AUD", "CAD", "CHF", "CNY", "EUR", "GBP", "HKD", "ILS", "INR", "JPY", "KRW", "MXN", "MYR", "NOK", "NZD", "RUB", "SEK", "SGD", "THB", "TRY", "USD", "ZAR"]
    
    var rate = 0.0
    var newCurrency = "" // a value from pickerData
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        //Connect data for the pickerview
        pickerView1.showsSelectionIndicator = true
        
        self.pickerView1.delegate = self as? UIPickerViewDelegate
        
        self.pickerView1.dataSource = self as? UIPickerViewDataSource
        
    }

    
    @IBAction func myButton1(_ sender: Any) {
    
        //Get the input from user
        let textFieldValue = Double(textfield1.text!)
        
        // If value is taken on 3-27-20
        if textFieldValue != nil{
        // Value for yen
            let result = Double(textFieldValue! * rate)
            
            myLabel1.text = "$\(textFieldValue!) = \(result)"
            
            textfield1.text = ""
        }
        
        else{
            
            myLabel1.text = "Value to convert can not be blank!"
        }
        
    // Start of pickerview supporting functions
    }
    //Number of seperate columns in the picker
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
           return 1
       }
    //number of items
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return pickerData.count
    }
    //names of each function
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return pickerData[row]
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        let urlLink  = "https://api.exchangerate-api.com/v4/latest/USD"
        let url = URL(string: urlLink)
        
        URLSession.shared.dataTask(with: url!){ (data,response,error) in
            
            do{
                //Download rates
                let ratesData = try JSONDecoder().decode(currencyRates.self, from: data!)
                
                print(ratesData.rates[self.newCurrency]!)
                self.rate = ratesData.rates[self.newCurrency]!
            } catch {
                print("Error")
            }
        }.resume()
        
        switch(pickerData[row]) {
            case "ZAR":
                newCurrency = "ZAR"
                break;
            case "USD":
                newCurrency = "USD"
               
                break;
            case "TRY":
                newCurrency = "TRY"
               
                break;
            case "THB":
                newCurrency = "THB"
               
                break;
            case "SGD":
                newCurrency = "SGD"
               
                break;
            case "SEK":
                newCurrency = "SEK"
               
                break;
            case "RUB":
                newCurrency = "RUB"
               
                break;
            case "NZD":
                newCurrency = "NZD"
               
                break;
            case "NOK":
                newCurrency = "NOK"
               
                break;
            case "MYR":
                newCurrency = "MYR"
               
                break;
            case "MXN":
                newCurrency = "MXN"
               
                break;
            case "KRW":
                newCurrency = "KRW"
               
                break;
            case "JPY":
                newCurrency = "JPY"
               
                break;
            case "INR":
                newCurrency = "INR"
               
                break;
            case "ILS":
                newCurrency = "ILS"
               
                break;
            case "HKD":
                newCurrency = "HKD"
               
                break;
            case "GBP":
                newCurrency = "GBP"
               
                break;
            case "EUR":
                newCurrency = "EUR"
               
                break;
            case "CNY":
                newCurrency = "CNY"
               
                break;
            case "CHF":
                newCurrency = "CHF"
               
                break;
            case "CAD":
                newCurrency = "CAD"
               
                break;
            case "AUD":
                newCurrency = "AUD"
               
                break;
            default:
                newCurrency = "USD"
                break;
        }
        
        self.view.endEditing(true)
        pickerView1.isHidden = false;
    } // End of picker function
    
    
}

