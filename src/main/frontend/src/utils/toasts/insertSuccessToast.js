import { toast} from 'vue3-toastify';

export function insertSuccessToast (texto) {
  toast.success(texto + ' INSERIDO(A) com sucesso!', {
    autoClose: 3000
  });
}