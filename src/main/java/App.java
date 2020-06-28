import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App {
    private ItemRepository itemRepository;
    private SalesPromotionRepository salesPromotionRepository;

    public App(ItemRepository itemRepository, SalesPromotionRepository salesPromotionRepository) {
        this.itemRepository = itemRepository;
        this.salesPromotionRepository = salesPromotionRepository;
    }

    public String bestCharge(List<String> inputs) {
        //TODO: write code here
        String selectItem="";
        String selectPro="";
        String firstSelectItem="";
        String secondSelectPro="";
        int count = 0;
    	
    	//初始化菜单及折扣
        List<Item> items = itemRepository.findAll();
        List<SalesPromotion> salesPromotion = salesPromotionRepository.findAll();

    	//接受用户输入并处理
        HashMap<String, Integer> inputItem = new HashMap<>();
        String id = "";
        int nums = 0;
        String[] temp = new String[2];

        for (String s : inputs) {
            temp = s.split(" x ");
            id = temp[0];
            nums = Integer.valueOf(temp[1]);
            inputItem.put(id, nums);
        }
        
        
        //输出用户订单
        for (Item item : items) {
            for (String key : inputItem.keySet()) {
                if (key.equals(item.getId())) {
                	firstSelectItem += (item.getName() + " x " + inputItem.get(key) + " = " + (int)Math.floor(inputItem.get(key) * item.getPrice()) + " yuan\n");
                }
            }
        }
        
        
        //计算总价
        int total = 0;
        HashMap<String, Double> price = new HashMap<>();
        HashMap<String, String> name = new HashMap<>();
        for (Item item : items) {
            price.put(item.getId(), item.getPrice());
            name.put(item.getId(), item.getName());
        }
        
        
        for (String i : inputItem.keySet()) {
                total += inputItem.get(i) * price.get(i);
        }
        
        
        //计算使用半价价格
        int promotionPrice = 0;
        int halfPromotionPrice = 0;
        List<String> relatedItems = null;
        StringBuffer promotionName = new StringBuffer();
        for (SalesPromotion sp : salesPromotion) {
            if (!(sp.getRelatedItems().size() == 0)) {
                relatedItems = sp.getRelatedItems();
            }
        }
        
        for (String i: inputItem.keySet()) {
            if (relatedItems.contains(i)) {
            	promotionName.insert(0,name.get(i) + "，");
                promotionPrice += inputItem.get(i) * price.get(i);
            } else {
            	halfPromotionPrice += inputItem.get(i) * price.get(i);
            }
        }
        halfPromotionPrice = (int) (halfPromotionPrice + promotionPrice*0.5);

        
        //计算使用满减价格
        int fullPromotionPrice = 0;
        if(total >= 30) {
        	fullPromotionPrice = total - 6;
        }
        
        //对比价格并输出
        if(halfPromotionPrice < fullPromotionPrice ) {
        	selectItem = firstSelectItem;
        	selectPro = "Promotion used:\n"+"Half price for certain dishes ("+promotionName.deleteCharAt(promotionName.length() - 1).toString()+")，saving "+(int)(promotionPrice*0.5)+" yuan\n"+"-----------------------------------\n";
        	count = halfPromotionPrice;
       }else {
        	if(total >= 30) {
            	selectItem = firstSelectItem;
            	selectPro = "Promotion used:\n"+"满30减6 yuan，saving 6 yuan\n"+"-----------------------------------\n";
            	count = fullPromotionPrice;
        		
        	}else if(halfPromotionPrice < total){
            	selectItem = firstSelectItem;
            	selectPro = "Promotion used:\n"+"Half price for certain dishes ("+promotionName.deleteCharAt(promotionName.length() - 1).toString()+")，saving "+promotionPrice*0.5+" yuan\n"+"-----------------------------------\n";
            	count = halfPromotionPrice;
        	}
        	else {
        		selectItem = firstSelectItem;
        		selectPro = "";
            	count = total;
        	}
        }
        
             
        String output = "============= Order details =============\n" +
                selectItem+
                "-----------------------------------\n" +
                selectPro +
                "Total："+count+" yuan\n" +
                "===================================";


        System.out.println(output);
        return output;
    }    


}
