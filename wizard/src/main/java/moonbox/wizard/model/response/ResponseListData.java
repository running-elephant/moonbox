package moonbox.wizard.model.response;

import lombok.Data;

import java.util.List;


@Data
@SuppressWarnings("unchecked")
public class ResponseListData<E> {
    private List<E> list;
    private int size;

    public ResponseListData(){}
    public ResponseListData(List<E> list){
        this.list=list;
        this.size=list!=null?list.size():0;
    }

    public static <E> ResponseListData getResponseListData(List<E> list){
        return new ResponseListData(list);
    }
}
