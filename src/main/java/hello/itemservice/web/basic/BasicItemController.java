package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    /* @RequiredArgsConstructior로 인해 final 변수 가지고 자동으로 생성자 만들어줌
    @Autowired 생성자 하나니까 @AutoWired 생략 가능
    public BasicItemController(ItemRepository itemRepository) { //생성자 주입
        this.itemRepository = itemRepository;
    }
    */

    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items",items);
        return "basic/items";
    }

    //상품상세(상품목록에서 상품명 누르면 상세설명)
    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId,Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "basic/item";
    }


    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init(){
        itemRepository.save(new Item("itemA",10000,10));
        itemRepository.save(new Item("itemA",20000,20));
    }
    //상품 등록 폼으로
    @GetMapping("/add")
    public String addForm(){
        return "basic/addForm";
    }
/*    @PostMapping ("/add")
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model){
        Item item=new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item",item);
        return "basic/item";
    }*/

/*    @PostMapping ("/add")
    public String addItemV2(@ModelAttribute("item") Item item,
                            Model model){
        itemRepository.save(item);

        //model.addAttribute("item",item); // 자동추가 ,생략가능
        return "basic/item";
    }*/


/*    @PostMapping ("/add")
    public String addItemV3(@ModelAttribute Item item,
                            Model model){
        //Item -> item
        itemRepository.save(item);

        //model.addAttribute("item",item); // 자동추가 ,생략가능
        return "basic/item";
    }*/

    //리다이렉트 안하고 PRG 패턴 적용 안한...
/*    @PostMapping ("/add") //ModelAttribute 생략
    public String addItemV4(Item item,
                            Model model){
        //Item -> item
        itemRepository.save(item);

        //model.addAttribute("item",item); // 자동추가 ,생략가능
        return "basic/item";
    }*/

/*    @PostMapping ("/add") //ModelAttribute 생략
    public String addItemV5(Item item,
                            Model model){
        //Item -> item
        itemRepository.save(item);

        return "redirect:/basic/items/"+item.getId();
    }*/
    //redirect할때 파라미터 붙혀서 보내버리면 그 파라미터 꺼내와서 저장했다고 안내창 띄워주면 됨
    @PostMapping ("/add") //ModelAttribute 생략
    public String addItemV6(Item item,
                            RedirectAttributes redirectAttributes){

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId",savedItem.getId());
        redirectAttributes.addAttribute("status",true);

        return "redirect:/basic/items/{itemId}"; //?status=true

    }
    //상품수정
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId,Model model){
        Item item= itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "basic/editForm";
    }
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId,@ModelAttribute Item item){
        itemRepository.update(itemId,item);
        return "redirect:/basic/items/{itemId}";
    }

}
