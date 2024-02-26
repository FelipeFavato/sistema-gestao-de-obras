import { toast} from 'vue3-toastify';

export function updateSuccessToast (texto) {
  toast.success(texto + ' ALTERADO(A) com sucesso!', {
    autoClose: 3000
  });
}