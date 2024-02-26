import { toast} from 'vue3-toastify';

export function deleteSuccessToast (texto) {
  toast.success(texto + ' DELETADO(A) com sucesso!', {
    autoClose: 3000
  });
}